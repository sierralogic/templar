(ns templar.core-test
  (:require [clojure.test :refer :all]
            [templar.core :refer :all]
            [templar.template.foobar.dispatcher :as foobar]))

(defn setup!
  []
  (register! foobar/template-id foobar/template-fs))

(defn teardown!
  [])

(defn test-fixture
  [f]
  (setup!)
  (f)
  (teardown!))

(use-fixtures :once test-fixture)

(deftest simple-core-test
  (testing "when someone shouts and whispers"
    (foobar/namespace! :templar.template.foobar.shout)
    (is (= "FOO (\"HI\")" (foobar/foo "hi")))
    (is (= "BAR (\"HELLO THERE\")" (foobar/bar "hello there")))
    (is (= "ANSWER IS 42!" (foobar/ans)))
    (foobar/namespace! :templar.template.foobar.whisper)
    (is (= "pssst. foo (\"hi\")" (foobar/foo "Hi")))
    (is (= "pssssst. bar (\"hello\")" (foobar/bar "HellO")))
    (is (= "psst. the answer is 42." (foobar/ans)))))
