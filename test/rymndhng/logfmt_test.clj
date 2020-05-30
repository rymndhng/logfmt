(ns rymndhng.logfmt-test
  (:require [clojure.test :refer [deftest are]]
            [rymndhng.logfmt :as sut]))

(deftest encode-test
  ;; See  https://github.com/go-logfmt/logfmt/blob/master/encode_test.go
  (are [expected input] (= expected (sut/encode input))
    "k=v"  {:k "v"}
    "k=v"  {"k" "v"}
    "\\=v" {"\\" "v"}

    "k="       {:k ""}
    "k=null"   {:k nil}
    "k=:value" {:k :value}
    "k=true"   {:k true}
    "k=1.025"  {:k 1.025}
    "k=0.001"  {:k 0.001}
    "k=4/5"    {:k 4/5}

    "k=\"v v\""      {:k "v v"}
    "k=\" \""        {:k " "}
    "k=\"\\\"\""     {:k "\""}
    "k=\"=\""        {:k "="}
    "k=\\"           {:k "\\"}
    "k=\"=\\\""      {:k "=\\"}
    "k=\ufffd"       {:k (str \ufffd)}
    "k=\ufffd\u0000" {:k (str \ufffd \u0000)}

    "k=foo v=bar" {:k "foo" :v "bar"}))
