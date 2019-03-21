(ns templar.template.foobar.whisper
  (:require [clojure.string :as str]))

(defn foo [& args] (let [x (str/lower-case (str "pssst. foo " args))]
                     (println x)
                     x))
(defn bar [& args] (let [x (str/lower-case (str "pssssst. bar " args))]
                     (println x)
                     x))
(defn ans [& args] (let [x "psst. the answer is 42."]
                     (println x)
                     x))
