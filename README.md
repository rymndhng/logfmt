# logfmt

[![Clojars Project](https://img.shields.io/clojars/v/rymndhng/logfmt.svg)](https://clojars.org/rymndhng/logfmt)

Generates logfmt encoded strings in Clojure.

## Why

`logfmt` is a nice middle point between unstructructed & structured data for
humans to read & execute adhoc log analysis.

See https://brandur.org/canonical-log-lines

## Usage

Basic Usage to Generate Strings

``` clojure
(require '[rymndhng.logfmt :as logfmt])

;; encoding maps
(logfmt/encode {:foo "bar"})
"foo=bar"

(logfmt/encode {:msg "hello world!"})
"msg=\"hello world!\""

;; encoding keyvalue pairs (to preserve ordering)
(logfmt/encode-keyvals :foo "bar" :baz "qux")
"foo=bar baz=qux"
```

## Integration with Logging Frameworks

`logfmt` is lightweight and is easily embedded inside your existing logging
solution.

```
(require '[rymndhng.logfmt :as logfmt])
(require '[clojure.tools.logging :as log])

(log/info (logfmt/encode-keyvals :msg "processed message" :time 500))
```

If you use `timbre`, there is an optional module to output logs in `logfmt`.

```clojure
(require '[taoensso.timbre :as timbre])
(require 'rymndhng.logfmt.timbre)

(timbre/merge-config! {:output-fn rymndhng.logfmt.timbre/logfmt-output-fn})

(timbre/warn "hello")
;> level=warn time="20-05-30 17:18:23" ns=user:156 msg=hello

(timbre/with-context {:id 12345}
  (timbre/warn "hello"))
;> level=warn time="20-05-30 17:19:56" ns=user:168 msg=hello id=12345

(let [ex (ex-info "uh-oh" {:user-id 123})]
  (timbre/warn ex "failed to handle command"))
;> level=warn time="20-05-30 17:21:53" ns=user:173 msg="failed to handle command"  error_class=clojure.lang.ExceptionInfo error_message=uh-oh


;; Configure output-fn to emit ex-data. Use with caution!
(timbre/merge-config! {:output-fn (partial rymndhng.logfmt.timbre/logfmt-output-fn {:ex-data? true})})

(let [ex (ex-info "uh-oh" {:user-id 123})]
  (timbre/warn ex "uhoh"))
;> level=warn time="20-05-30 17:39:38" ns=user:323 msg=uhoh error_class=clojure.lang.ExceptionInfo error_message=uh-oh user-id=123
```

## License

Copyright © 2020 Raymond Huang

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
