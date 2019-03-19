(ns templar.template.foobar.dispatcher
  (:require [templar.core :as templar]))

(def default-template-ns :templar.template.foobar.shout)

(def template-id :foobars)

(def template-fs [{:fn :foo}
                  {:fn :bar}
                  {:fn :ans}])

(templar/register! template-id template-fs)

(when-let [check (templar/register-namespace! default-template-ns template-id)]
  (println "WARNING: " check))

(defn namespace!
  [ns]
  (templar/register-namespace! ns template-id))

(defn dispatch
  [fn & args]
  (apply templar/apply-template-function (concat [template-id fn] args)))

(def foo "calls (foo x y z)" (partial dispatch :foo))
(def bar "calls (bar x y)" (partial dispatch :bar))
(def ans "calls (ans)" (partial dispatch :ans))
