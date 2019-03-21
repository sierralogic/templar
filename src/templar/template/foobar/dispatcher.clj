(ns templar.template.foobar.dispatcher
  (:require [templar.core :as templar]))

(def template-id :foobars)

(def template-fs [{:fn :foo
                   :description "This is the foo function, meh."}
                  {:fn :bar
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
                  {:fn :ans
                   :args []
                   :description "Answer, without the question.  Bring a towel."}])

(templar/register! template-id template-fs)

(def default-template-ns :templar.template.foobar.shout)

(defn namespace!
  ([ns] (namespace! ns nil))
  ([ns meta]
   (templar/register-namespace! ns template-id meta)))

(when-let [check (namespace! default-template-ns {:description "default foobars ns"})]
  (println "WARNING: " default-template-ns ":: " check))

(defn dispatch
  [fn & args]
  ;; (println :dispatch :fn fn :args args)
  (apply templar/apply-template-function (concat [template-id fn] args)))

(def foo (partial dispatch :foo))
(def bar (partial dispatch :bar))
(def ans (partial dispatch :ans))
