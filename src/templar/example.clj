(ns templar.example
  (:require [templar.template.foobar.dispatcher :as foobar]))

(defn run
  "Example run to demonstrate templar impelemenation."
  []
  (foobar/namespace! :templar.template.foobar.shout)
  (println "SHOUTING!!!")
  (foobar/foo 1 2 3 4)
  (foobar/bar 3 4 2 3 :a :b)
  (foobar/ans)

  (foobar/namespace! :templar.template.foobar.whisper)
  (println "whispering...")
  (foobar/foo 1 2 3 4)
  (foobar/bar 3 4 2 3 :a :b)
  (foobar/ans))
