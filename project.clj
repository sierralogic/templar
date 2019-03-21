(defproject templar "0.1.2"
  :description "Templar is a logical grouping of functions into namespaces library for dependency inversion."
  :url "http://sierralogic.github.io/templar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :target-path "target/%s"
  :plugins [[lein-codox "0.10.6"]]
  :profiles {:uberjar {:aot :all}})
