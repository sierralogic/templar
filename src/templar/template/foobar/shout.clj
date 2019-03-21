(ns templar.template.foobar.shout
  (:require [clojure.string :as str]))

(defn foo [& args] (let [x (str/upper-case (str "foo " args))]
                     (println x)
                     x))
(defn bar [& args] (let [x (str/upper-case (str "bar " args))]
                     (println x)
                     x))
(defn ans [& args] (let [x "ANSWER IS 42!"]
                     (println x)
                     x))
