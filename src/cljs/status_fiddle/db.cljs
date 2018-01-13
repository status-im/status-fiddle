(ns status-fiddle.db)

(def default-db
  {:name "re-frame"
   :text "[:h1 \"Hello World!\"]" ;; text of the editor
   :mode :indent-mode             ;; editor mode (:indent-mode, :paren-mode)
   :cm nil                        ;; the CodeMirror instance
   :watcher nil})                 ;; the ScrollMonitor instance
