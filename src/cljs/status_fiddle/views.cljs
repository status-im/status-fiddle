(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-fiddle.gist :as gist]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]
            [status-fiddle.icons :as icons]
            [status-fiddle.status-colors :as status-colors]
            [status-fiddle.status-icons :as status-icons]
            [status-im.ui.components.styles :as styles]))

(defn code-mirror []
  (reagent/create-class
    {:reagent-render      (fn []
                            [:div#code-pane])
     :component-did-mount (fn [_]
                            (let [cm (js/CodeMirror (.getElementById js/document "code-pane"))]
                              (re-frame/dispatch [:set-cm cm])
                              (.init js/parinferCodeMirror cm)
                              (.setValue cm @(re-frame/subscribe [:source]))
                              (.on cm "change" #(re-frame/dispatch [:update-source (.getValue cm)]))))}))

(defn valid-hiccup? [vec]
  (let [first-element (nth vec 0 nil)]
    (cond
      (not (vector? vec)) false
      (not (pos? (count vec))) false
      (string? first-element) false
      (not (reagent.impl.template/valid-tag? first-element)) false
      (not (every? true? (map valid-hiccup? (filter vector? vec)))) false
      :else true)))

(defn compiler []
  (let [cljs-string @(re-frame/subscribe [:source])
        compiled-hic (eval-str (empty-state)
                               (str "(ns cljs.user
                          (:refer-clojure :exclude [atom])
                          (:require reagent.core [status-fiddle.react-native-web :as react] [status-fiddle.icons :as icons] [status-im.ui.components.styles :as styles]))
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
                                     (js/console.error "Error: " (str error)))
                                   value)))]
    (if (valid-hiccup? compiled-hic)
      (do
        (re-frame/dispatch [:update-result compiled-hic])
        (re-frame/dispatch [:save-the-source])
        (re-frame/dispatch [:delete-error-message]))
      (re-frame/dispatch [:set-error "Hiccup expression is invalid"]))))

(defn share-panel []
  [react/view {:style {:flex-direction :row :justify-content :flex-end}}
   [react/text @(re-frame/subscribe [:url])]
   [react/touchable-highlight {:on-press #(re-frame/dispatch [:share-source-on-gist])}
    [react/view {:style {:background-color styles/color-light-blue :border-radius 8 :padding-horizontal 10 :padding-vertical 2
                         :margin-bottom 2 :margin-left 10}}
     [react/text {:style {:color :white :font-size 11}}
      "Share"]]]])

(defn dom-pane []
  (let [result (re-frame/subscribe [:result])]
    (fn []
      [react/view {:style {:flex 1}}
       @result])))

(defn error-view []
  (let [error @(re-frame/subscribe [:error])]
     [react/text {:style {:color :red}} error]))

(defn main-panel []
  [react/view {:style {:padding 50}}
   [status-colors/colors-panel]
   [status-icons/icons-panel]
   [react/view {:style {:flex-direction :row}}
    [react/view {:style {:flex 1}}
     [compiler]
     [share-panel]
     [react/view {:style {:flex 1}}
      [code-mirror]]]
    [react/view {:style {:margin-left 50}}
     [react/text "iPhone 6"]
     [react/view {:style {:width 375 :height 667 :border-width 1 :border-color :blue}}
      [dom-pane]]
     [error-view]]]])
