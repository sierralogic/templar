(ns templar.template.foobar.dispatcher
  (:require [templar.core :as templar]))

(def template-id :foobars)

(def template-fs [{:fn "foo"
                   :description "This is the foo function, meh."}
                  {:fn "bar"
                   :args [{:name "x"
                           :type :map
                           :description "The x of the bar call."}
                          {:name "y"
                           :type :string
                           :description "This y of the bar call."}
                          {:name "z"
                           :optional? true
                           :type :long
                           :description "This is the optional z for the bar call"}]
                   :description "This is the bar function, blah."}
                  {:fn "ans"
                   :args []
                   :description "Answer, without the question.  Bring a towel."}])

(def default-template-ns "templar.template.foobar.shout")

(defn namespace!
  "Register the namespace `ns` to :foobars template with optional metadata map `meta`."
  ([ns] (namespace! ns nil))
  ([ns meta]
   (templar/register-namespace! ns template-id meta)))

(defn init
  "Initialize example foobar dispatcher."
  []
  (templar/register! template-id template-fs) ; register template with templar
  (when-let [check (namespace! default-template-ns {:description "default foobars ns"})]
    (println "WARNING: " default-template-ns ":: " check)))

(defn dispatch
  "Dispatch template function call `fn` for :foobars template with optional
  function arguments `args`."
  [fn & args]
  (apply templar/apply-template-function (concat [template-id fn] args)))

(def foo (partial dispatch :foo))
(def bar (partial dispatch :bar))
(def ans (partial dispatch :ans))

(init)
