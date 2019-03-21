(ns templar.template.foobar.dumb
  (:require [clojure.string :as str]))

(defn foo [& args] (let [x (str/lower-case (str "duh. foo " args))]
                     (println x)
                     x))
(defn bar [& args] (let [x (str/lower-case (str "duh. bar " args))]
                     (println x)
                     x))
;;; note lack of template :foobars function `ans`
