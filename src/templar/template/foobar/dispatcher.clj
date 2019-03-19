(ns templar.template.foobar.dispatcher
  (:require [templar.core :refer [template register! apply-template-function] :as templates]))

(def default-template-ns :templar.template.foobar.shout)

(def template-id :foobars)

(def template-fs [{:fn :foo}
                  {:fn :bar}
                  {:fn :ans}])

(register! template-id template-fs)

(when-let [check (templates/register-namespace! default-template-ns template-id)]
  (println "WARNING: " check))

(defn namespace!
  [ns]
  (templates/register-namespace! ns template-id))

(defn dispatch
  [fn & args]
  (apply apply-template-function (concat [template-id fn] args)))

(def foo "calls (foo x y z)" (partial dispatch :foo))
(def bar "calls (bar x y)" (partial dispatch :bar))
(def ans "calls (ans)" (partial dispatch :ans))
