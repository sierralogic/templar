(defproject templar "0.1.0-SNAPSHOT"
  :description "Templar is a logical grouping of functions into namespaces library for dependency injection."
  :url "http://sierralogic.com/post/templar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot templar.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
