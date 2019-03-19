(ns templar.template.foobar.whisper
  (:require [clojure.string :as str]))

(defn foo [& args] (println (str/lower-case (str "pssst. foo " args))))
(defn bar [& args] (println (str/lower-case (str "pssssst. bar" args))))
(defn ans [& args] 42)
