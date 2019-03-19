(ns templar.core)

(defn ->str
  [x]
  (when x
    (if (keyword? x)
      (subs (str x) 1)
      (str x))))

(def namespaces (atom {}))

(def registry (atom {}))

(defn lookup
  ([id] (lookup id nil))
  ([id opt]
   (get @registry id opt)))

(defn template
  ([id] (template id nil))
  ([id opt]
   (if-let [entry (lookup id opt)]
     (get entry :t opt)
     opt)))

(defn register!
  ([id t] (register! id nil t))
  ([id meta t]
   (swap! registry assoc id {:id id :m meta :t t})))


(def function-cache "Cache of resolved/reified functions." (atom nil))

(defn ->str
  "Converts x to string.  If x is a keyword, then does a smart convert with slash between
  namespace and name:  ex. (-> :this) => this and (-> :foo/bar) => foo/bar"
  [x]
  (if x
    (if (keyword? x)
      (str (if (namespace x) (str (namespace x) "/") "") (name x))
      (str x))))

(defn clear-function-cache!
  "Clears the function cache."
  []
  (reset! function-cache nil))

(defn ->function-id
  "Converts the namespace ns-name (string or kw) and function name
  func-name (string or kw) to a normalized function id.
  Ex. (->function-id :this.that :foobar) => this.that/foobar"
  [ns-name func-name]
  (str (->str ns-name) "/" (->str func-name)))

(defn cache-function!
  "Caches the function f given the namespace ns-name (string or kw) and
  the function name func-name (string or kw)."
  [ns-name func-name f]
  (swap! function-cache assoc (->function-id ns-name func-name) f))

(defn check-function-cache
  "Checks the function cache for cached function value given
  the namespace ns-name (string or kw) and function name func-name (string or kw).
  Returns nil if not found.
  Otherwise returns cached function."
  [ns-name func-name]
  (get @function-cache (->function-id ns-name func-name)))

(defn safe-apply
  "Wraps a function f call with args (seq) with a try-catch.
  Returns nil on thrown exception from f call.
  Otherwise returns result of (apply f args)."
  [f args]
  (try
    (apply f args)
    (catch Exception e)))

(defn safe-require
  "Wraps a namespace require call in try-catch.
  Returns nil if namespace successfully resolved and required.
  Otherwise, returns string with thrown exception message."
  [ns]
  (try
    (require (if (symbol? ns) ns (symbol (->str ns))))
    (catch Exception e
      (str "Require of namespace '" (->str ns) "' failed. [" (.getMessage e) "]"))))

(defn resolve-function
  "Resolves and returns (if successful) function with namespace ns-name (string or kw)
  function name func-name (string or kw) and optional cache? flag to involve
  resolved function caching (for performance).
  Returns nil if function was not retrieved from cache and/or not resolved/reified.
  Otherwise returns the resolved and reified function."
  [ns-name func-name & [cache?]]
  (if-let [cached-f (when cache? (check-function-cache ns-name func-name))]
    cached-f
    (let [nst (->str ns-name)]
      (when-not (safe-require nst)
        (when-let [f (resolve (symbol nst (->str func-name)))]
          (when (and cache? f) (cache-function! ns-name func-name f))
          f)))))

(defn compliant
  [ns template-id]
  (if-let [template-fs (template template-id)]
    (reduce #(if-let [f (resolve-function (->str ns) (->str (get %2 :fn)))]
               %
               (conj (or % []) {:message (str "Missing function " %2 ".")}))
            nil
            template-fs)
    [{:message (str "No template found in registries for id '" template-id "'.")}]))

(defn unregister!
  [id]
  (swap! registry dissoc id))

(defn register-namespace!
  [ns id]
  (if-let [check (compliant ns id)]
    check
    (do
      (swap! namespaces assoc id {:id id :ns ns})
      nil)))

(defn registered-namespace-of
  [id]
  (get (get @namespaces id) :ns))

(defn apply-template-function
  [id fn & args]
  (when-let [t (template id)]
    (when-let [ns (registered-namespace-of id)]
      (when-let [f (resolve-function (->str ns) (->str fn))]
        (apply f args)))))
