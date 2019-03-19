(ns templar.template.foobar.shout
  (:require [clojure.string :as str]))

(defn foo [& args] (println (str/upper-case (str "foo " args))))
(defn bar [& args] (println (str/upper-case (str "bar " args))))
(defn ans [& args] (println "ANSWER IS 42!"))