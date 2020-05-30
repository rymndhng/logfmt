(defproject rymndhng/logfmt "0.1.0-SNAPSHOT"
  :description "logfmt output for Clojure"
  :url "https://github.com/rymndhng/logfmt"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :deploy-repositories {"releases" {:url "https://repo.clojars.org" :creds :gpg}}

  :profiles
  {:dev
   {:dependencies [[com.taoensso/timbre "4.10.0"]
                   [org.clojure/clojure "1.10.1"]]}


   :1.8  {:dependencies [[org.clojure/clojure "1.8"]]}
   :1.10 {:depenedencie [[org.clojure/clojure "1.10.1"]]}

   })
