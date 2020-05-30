(ns rymndhng.logfmt
  "Creates logfmt-style logs from Clojure key-value pairs

  For more context, see https://brandur.org/logfmt and https://brandur.org/canonical-log-lines
  "
  (:require [clojure.string :as string]))

(defn logfmt-quote [s]
  (if (and (string? s)
           (re-find #"[\s=\"]" s))
    (format "\"%s\"" (string/replace s #"([\"])" "\\\\$1"))
    s))

(defn encode
  "Encodes a sequence of entries as a logfmt string.

  The sequence can come from map or a list of tuples"
  [m]
  (->> m
       (mapv (fn [[k v]] (format "%s=%s" (name k) (logfmt-quote v))))
       (string/join " ")))

(defn encode-keyvals [& keyvals]
  (encode (partition 2 keyvals)))

(comment
  (logfmt-quote "oh")
  (logfmt-quote "val")
  (println (logfmt-quote "oh \" hi"))
  (logfmt-quote "="))
