(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]))

(defn code-mirror []
  (reagent/create-class
    {:reagent-render      (fn []
                            [:div#code-panel])
     :component-did-mount (fn [_]
                            (let [cm (js/CodeMirror (.getElementById js/document "code-panel"))]
                              (.setValue cm @(re-frame/subscribe [:text]))
                              (.on cm "change" #(re-frame/dispatch [:update-text (.getValue cm)]))))}))

(defn dom-pane []
  [:div#result-panel
   (let [cljs-string @(re-frame/subscribe [:text])]
     (eval-str (empty-state)
               (str "(ns cljs.user
                    (:refer-clojure :exclude [atom])
                    (:require reagent.core [status-fiddle.react-native-web :as react]))
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
  [react/view {:style {:flex-direction :row :padding-vertical 50}}
   [react/view {:style {:flex 1 :padding-horizontal 50}}
    [code-mirror]]
   [react/view {:style {:flex 1 :padding-horizontal 50}}
    [dom-pane]]])
