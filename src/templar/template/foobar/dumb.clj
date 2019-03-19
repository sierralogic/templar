(ns templar.template.foobar.dumb
  (:require [clojure.string :as str]))

(defn foo [& args] (println (str/lower-case (str "duh. foo " args))))
(defn bar [& args] (println (str/lower-case (str "duh. bar " args))))

;;; note lack of template :foobars function `ans`
