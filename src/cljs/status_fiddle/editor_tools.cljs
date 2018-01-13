(ns status-fiddle.editor_tools
  (:require
   [clojure.string :refer [join]]
   [re-frame.core :as re-frame]
   [status-fiddle.editor :refer [start-editor-sync! parinferize!]]
   [status-fiddle.editor-support :refer [update-cursor!
                                                          fix-text!
                                                          cm-key
                                                          IEditor
                                                          get-prev-state
                                                          frame-updated?
                                                          set-frame-updated!]]))
(defn on-tab
  "Indent selection or insert two spaces when tab is pressed.
  from: https://github.com/codemirror/CodeMirror/issues/988#issuecomment-14921785"
  [cm]
  (if (.somethingSelected cm)
    (.indentSelection cm)
    (let [n (.getOption cm "indentUnit")
          spaces (apply str (repeat n " "))]
      (.replaceSelection cm spaces))))

(def editor-opts
  {:mode "clojure-parinfer"
   :matchBrackets true
   :viewportMargin Infinity
   :indentUnit 2
   :height "100%"
   :extraKeys {:Tab on-tab
               :Shift-Tab "indentLess"}})

(defn create-editor!
  "Create a parinfer editor."
  ([element-id key-] (create-editor! element-id key- {}))
  ([element-id key- opts]
   (when (get  @(re-frame/subscribe [:db]) key-)(re-frame/dispatch [:dissoc key-]))
   (let [element (js/document.getElementById element-id)
         cm (js/CodeMirror.fromTextArea element (clj->js (merge editor-opts opts)))
         wrapper (.getWrapperElement cm)]
     (set! (.-id wrapper) (str "cm-" element-id))
     (parinferize! cm key- (:parinfer-mode opts) "")
     cm)))