(ns templar.template.foobar.dispatcher
  (:require [templar.core :as templar]))

(def template-id "Foobars template id constant :foobars" :foobars)

(def template-fs "Foobars template functions" [{:fn "foo"
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

(def default-template-ns "Default :foobars template namespace of shout" "templar.template.foobar.shout")

(defn namespace!
  "Register the namespace `ns` to :foobars template with optional metadata map `meta`."
  ([ns] (namespace! ns nil))
  ([ns meta]
   (templar/register-namespace! ns template-id meta)))

(defn dispatch
  "Dispatch template function call `fn` for :foobars template with optional
  function arguments `args`."
  [fn & args]
  (apply templar/apply-template-function (concat [template-id fn] args)))

(def foo "Partial on function `dispatch` with first argument of :foo" (partial dispatch :foo))
(def bar "Partial on function `dispatch` with first argument of :bar" (partial dispatch :bar))
(def ans "Partial on function `dispatch` with first argument of :ans" (partial dispatch :ans))

(defn init
  "Initialize example foobar dispatcher."
  []
  (templar/register! template-id template-fs) ; register template with templar
  (when-let [check (namespace! default-template-ns {:description "default foobars ns"})]
    (println "WARNING: " default-template-ns ":: " check)))

(init)
