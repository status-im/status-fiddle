(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]
            [status-fiddle.icons :as icons]
            [status-im.ui.components.styles :as styles]))

(defn code-mirror []
  (reagent/create-class
    {:reagent-render      (fn []
                            [:div#code-pane])
     :component-did-mount (fn [_]
                            (let [cm (js/CodeMirror (.getElementById js/document "code-pane"))]
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
        (re-frame/dispatch [:delete-error-message]))
      (re-frame/dispatch [:set-error "Hiccup expression is invalid"]))))

(defn dom-pane []
  (let [result (re-frame/subscribe [:result])]
    (fn []
      [react/view {:style {:flex 1}}
       @result])))

(defn error-view []
  (let [error @(re-frame/subscribe [:error])]
     [react/text {:style {:color :red}} error]))

(defn color-panel [color label]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:show-color color label])}
   [react/view {:style {:width 20 :height 20 :background-color color}}]])

(defn color-label []
  (let [{:keys [color label]} @(re-frame/subscribe [:color])]
    [react/view {:style {:flex-direction :row}}
     [react/view {:style {:width 20 :height 20 :background-color color}}]
     [react/text {:style {:margin-left 20}} color]
     [react/text {:style {:margin-left 20}} label]]))

(defn colors-panel []
  [react/view {:style {:margin-bottom 10}}
   [react/view {:style {:flex-direction :row :flex-wrap :wrap}}
    [color-panel styles/color-blue "styles/color-blue"]
    [color-panel styles/color-blue2 "styles/color-blue2"]
    [color-panel styles/color-blue3 "styles/color-blue3"]
    [color-panel styles/color-blue4 "styles/color-blue4"]
    [color-panel styles/color-blue4-faded "styles/color-blue4-faded"]
    [color-panel styles/color-blue4-transparent "styles/color-blue4-transparent"]
    [color-panel styles/color-blue5 "styles/color-blue5"]
    [color-panel styles/color-blue6 "styles/color-blue6"]
    [color-panel styles/color-blue-transparent "styles/color-blue-transparent"]
    [color-panel styles/color-black "styles/color-black"]
    [color-panel styles/color-purple "styles/color-purple"]
    [color-panel styles/color-gray-transparent-light "styles/color-gray-transparent-light"]
    [color-panel styles/color-gray-transparent-medium-light "styles/color-gray-transparent-medium-light"]
    [color-panel styles/color-gray-transparent "styles/color-gray-transparent"]
    [color-panel styles/color-gray4-transparent "styles/color-gray4-transparent"]
    [color-panel styles/color-gray10-transparent "styles/color-gray10-transparent"]
    [color-panel styles/color-gray "styles/color-gray"]
    [color-panel styles/color-gray2 "styles/color-gray2"]
    [color-panel styles/color-gray3 "styles/color-gray3"]
    [color-panel styles/color-gray4 "styles/color-gray4"]
    [color-panel styles/color-gray5 "styles/color-gray5"]
    [color-panel styles/color-gray6 "styles/color-gray6"]
    [color-panel styles/color-gray7 "styles/color-gray7"]
    [color-panel styles/color-gray8 "styles/color-gray8"]
    [color-panel styles/color-gray9 "styles/color-gray9"]
    [color-panel styles/color-gray10 "styles/color-gray10"]
    [color-panel styles/color-gray11 "styles/color-gray11"]
    [color-panel styles/color-dark "styles/color-dark"]
    [color-panel styles/color-steel "styles/color-steel"]
    [color-panel styles/color-white "styles/color-white"]
    [color-panel styles/color-white-transparent "styles/color-white-transparent"]
    [color-panel styles/color-white-transparent-1 "styles/color-white-transparent-1"]
    [color-panel styles/color-white-transparent-2 "styles/color-white-transparent-2"]
    [color-panel styles/color-white-transparent-3 "styles/color-white-transparent-3"]
    [color-panel styles/color-white-transparent-4 "styles/color-white-transparent-4"]
    [color-panel styles/color-white-transparent-5 "styles/color-white-transparent-5"]
    [color-panel styles/color-white-transparent-6 "styles/color-white-transparent-6"]
    [color-panel styles/color-light-blue "styles/color-light-blue"]
    [color-panel styles/color-light-blue-transparent "styles/color-light-blue-transparent"]
    [color-panel styles/color-light-blue2 "styles/color-light-blue2"]
    [color-panel styles/color-light-blue3 "styles/color-light-blue3"]
    [color-panel styles/color-light-blue4 "styles/color-light-blue4"]
    [color-panel styles/color-light-blue5 "styles/color-light-blue5"]
    [color-panel styles/color-light-blue6 "styles/color-light-blue6"]
    [color-panel styles/color-light-blue7 "styles/color-light-blue7"]
    [color-panel styles/color-dark-blue-1 "styles/color-dark-blue-1"]
    [color-panel styles/color-dark-blue-2 "styles/color-dark-blue-2"]
    [color-panel styles/color-dark-blue-3 "styles/color-dark-blue-3"]
    [color-panel styles/color-dark-blue-4 "styles/color-dark-blue-4"]
    [color-panel styles/color-light-gray "styles/color-light-gray"]
    [color-panel styles/color-light-gray2 "styles/color-light-gray2"]
    [color-panel styles/color-light-gray3 "styles/color-light-gray3"]
    [color-panel styles/color-light-gray4 "styles/color-light-gray4"]
    [color-panel styles/color-light-gray5 "styles/color-light-gray5"]
    [color-panel styles/color-red "styles/color-red"]
    [color-panel styles/color-red-2 "styles/color-red-2"]
    [color-panel styles/color-red-3 "styles/color-red-3"]
    [color-panel styles/color-red-4 "styles/color-red-4"]
    [color-panel styles/color-light-red "styles/color-light-red"]
    [color-panel styles/color-light-red2 "styles/color-light-red2"]
    [color-panel styles/color-green-1 "styles/color-green-1"]
    [color-panel styles/color-green-2 "styles/color-green-2"]
    [color-panel styles/color-green-3 "styles/color-green-3"]
    [color-panel styles/color-green-3-light "styles/color-green-3-light"]
    [color-panel styles/color-green-4 "styles/color-green-4"]
    [color-panel styles/color-cyan "styles/color-cyan"]
    [color-panel styles/color-separator "styles/color-separator"]
    [color-panel styles/text1-disabled-color "styles/text1-disabled-color"]
    [color-panel styles/selected-message-color "styles/selected-message-color"]
    [color-panel styles/separator-color "styles/separator-color"]]
   [color-label]])

(defn icon-panel [icon]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:show-icon icon])}
   [icons/icon icon]])

(defn icon-label []
  (let [icon @(re-frame/subscribe [:icon])]
    (when icon
      [react/view {:style {:flex-direction :row :align-items :center}}
       [icons/icon icon]
       [react/text {:style {:margin-left 20}} (str "[icons/icon " icon "]")]])))

(defn icons-panel []
  [react/view {:style {:margin-bottom 10}}
   [react/view {:style {:flex-direction :row :flex-wrap :wrap}}
    (for [icon (keys icons/icons)]
      [icon-panel icon])]
   [icon-label]])

(defn main-panel []
  [react/view {:style {:padding 50}}
   [colors-panel]
   [icons-panel]
   [react/view {:style {:flex-direction :row}}
    [compiler]
    [react/view {:style {:flex 1}}
     [code-mirror]]
    [react/view {:style {:margin-left 50}}
     [react/text "iPhone 6"]
     [react/view {:style {:width 375 :height 667 :border-width 1 :border-color :blue}}
      [dom-pane]]
     [error-view]]]])
