(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [status-fiddle.editor :refer [start-editor-sync!]]
            [status-fiddle.editor_tools :as et]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]))

(defn code-mirror []
  (reagent/create-class
    {:reagent-render      (fn [] [:div
                                  {:style {:border "1px solid #9A9A9A" :display "inline-block"}}
                                  [:textarea#codezone {:auto-focus    true
                                                       :default-value @(re-frame/subscribe [:text])}]])
     :component-did-mount (fn [_]
                            (et/create-editor! "codezone" :codemirror-box)
                            (start-editor-sync!))}))

(defn dom-pane []
  [:div {:style {:display "inline-block"}}
    (let [cljs-string @(re-frame/subscribe [:text])]
      (eval-str (empty-state)
                (str "(ns cljs.user
                    (:refer-clojure :exclude [atom])
                    (:require reagent.core))

                  (def atom reagent.core/atom)"
                     (or (not-empty cljs-string)
                         "[:div]"))
                'dummy-symbol
                {:ns            'cljs.user
                 :eval          js-eval
                 :static-fns    true
                 :def-emits-var false
                 :load          (fn [name cb] (cb {:lang :clj :source ""}))
                 :context       :statement}
                (fn [{:keys [error value] :as x}]
                  (if error
                    (do
                      (def *er x)
                      (js/console.error
                        "Error: " (str error)))
                    value))))])

(defn main-panel []
  [react/view
   [:div
    [code-mirror]
    [dom-pane]]])
