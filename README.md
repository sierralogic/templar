# templar

Templar is a dependency injection libary that uses templates (groups of function names and descriptions) to 
dynamically resolve/load functions.

## Example

Setup of template function `dispatcher` namespace.

```clojure
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

```

Setting up two implementations, `shout` and `whisper`.

`shout`

```clojure
(ns templar.template.foobar.shout
  (:require [clojure.string :as str]))

(defn foo [& args] (println (str/upper-case (str "foo " args))))
(defn bar [& args] (println (str/upper-case (str "bar " args))))
(defn ans [& args] (println "ANSWER IS 42!"))
```

`whisper`

```clojure
(ns templar.template.foobar.whisper
  (:require [clojure.string :as str]))

(defn foo [& args] (println (str/lower-case (str "pssst. foo " args))))
(defn bar [& args] (println (str/lower-case (str "pssssst. bar " args))))
(defn ans [& args] (println (str "psst. the answer is 42.")))
```

And running the `example` function `run`:

```clojure
(ns templar.example
  (:require [templar.template.foobar.dispatcher :as foobar]))

(defn run
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
```

Results in the output:

```clojure
(run)
;=>

SHOUTING!!!
FOO (1 2 3 4)
BAR (3 4 2 3 :A :B)
ANSWER IS 42!

whispering...
pssst. foo (1 2 3 4)
pssssst. bar (3 4 2 3 :a :b)
psst. the answer is 42.
```

## Implementing Templates

This example implementation above is just one of many ways to leverage `templar`.  This approach is minimal on the `dispatcher` by leveraging
`partial` to quickly stand up an implementation of the template, but at the cost of arity checking and more fleshed out
function signatures and documentation.

However, more granular implementation may have the `foo` and `bar` implemented as actual functions with arity-controlled
arguments.  This would allow IDEs and easy introspection to determine what the allowed arities are for each template
function.

For example, instead of:

```clojure
(def foo (partial dispatch :foo))
```

you can the following if `foo` expects two (2) arguments:

```clojure
(defn foo
  "This is the docstring for foo that developers might use since the implemented `foo` might be in libary jar somewhere."
  [x y]
  (apply dispatch [:foo x y]))
```

## Template Compliance

You can determine if a namespace is template compliant by calling the `(compliant ns template-id)`.

If the call returns `nil`, then the namespace is compliant.

```clojure
(compliant :templar.template.foobar.shout :foobars)
;=>
nil
```

If the namespace is NOT compliant, then the call will return a vector of failed compliants (missing functions).

```clojure
(ns templar.template.foobar.dumb
  (:require [clojure.string :as str]))

(defn foo [& args] (println (str/lower-case (str "duh. foo " args))))
(defn bar [& args] (println (str/lower-case (str "duh. bar " args))))
;;; note lack of template :foobars function `ans`
```

and running `compliant` on the `dumb` namespace:

```clojure
(templates/compliant :templar.template.foobar.dumb :foobars)
;=>
[{:message "Missing function {:fn :ans}."
  :fn :ans}]

```

## License

Copyright © 2019 SierraLogic LLC

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
