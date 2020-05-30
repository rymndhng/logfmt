(ns rymndhng.logfmt.timbre
  (:require
   [rymndhng.logfmt :refer [logfmt-quote encode]]
   [taoensso.timbre :as timbre]))

(defn logfmt-output-fn
  "Writes logs in logfmt

  Options:

  :ex-data?        If truthy, appends ex-data to logs (default: false)
  :no-stacktrace?  Disables outputting stacktraces    (default: false)

  Additional options to configure stacktrace output are identical to
  `taoensso.timbre/default-output-fn`.
  "
  ([     data]
   (logfmt-output-fn nil data))
  ([opts data]
   (let [{:keys [no-stacktrace? ex-data?]} opts
         {:keys [level timestamp_ ?err msg_ ?ns-str ?file ?line context]} data]
     (with-out-str
       (printf "level=%s time=%s ns=%s msg=%s %s"
               (name level)
               (logfmt-quote (force timestamp_))
               (logfmt-quote (str (or ?ns-str ?file "?") ":" (or ?line "?")))
               (logfmt-quote (force msg_))
               (encode context))
       (when-let [err ?err]
         (printf "error_class=%s error_message=%s"
                 (.getName (class err))
                 (logfmt-quote (.getMessage ^Throwable err)))
         (when-let [ex-data (ex-data err)]
           (when ex-data?
             (print "" (encode ex-data))))
         (when-not no-stacktrace?
           (newline)
           (print (timbre/stacktrace err opts))))))))

(comment

  (timbre/with-merged-config {:output-fn logfmt-output-fn}
    (timbre/warn (ex-info "doh" {:foo "bar"}) "failed task"))

  (timbre/with-merged-config {:output-fn (partial logfmt-output-fn {:ex-data? true})}
    (timbre/warn (ex-info "doh" {:foo "bar"}) "failed task"))

  (timbre/with-merged-config {:output-fn logfmt-output-fn}
    (taoensso.timbre/with-context {:foo "bar"}
      (timbre/info "")))

  (let [err1 (IllegalStateException. "oops")]
    (timbre/with-merged-config {:output-fn logfmt-output-fn}
      (taoensso.timbre/with-context {:foo "bar"}
        (timbre/warn (ex-info "doh" {:hello "world"} err1) "bro!!"))))

  (let [err1 (IllegalStateException. "oops")]
    (timbre/with-merged-config {:output-fn (partial logfmt-output-fn {:no-stacktrace? false})}
      (taoensso.timbre/with-context {:method "get"
                                     :path "/pages/123-12412-43123123123123/leads"
                                     :route "/pages/:page-id/leads"
                                     :agent "asdf asdf asd fas df asdf asdf asd"}
        (timbre/warn (ex-info "doh" {:hello "world"} err1) "bro!!"))))
  )
