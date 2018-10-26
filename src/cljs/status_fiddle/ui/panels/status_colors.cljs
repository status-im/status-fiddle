(ns status-fiddle.ui.panels.status-colors
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.colors :as colors]
            [status-im.ui.components.react :as react]))

(defn color-panel [color label]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:show-color color label])}
   [react/view {:style {:width 20 :height 20 :background-color color}}]])

(defview color-label []
  (letsubs [{:keys [color label]} [:get :color]]
    [react/view {:style {:flex-direction :row}}
     [react/view {:style {:width 20 :height 20 :background-color color}}]
     [react/text {:style {:margin-left 20}} color]
     [react/text {:style {:margin-left 20}} label]]))

(def colors-ns-colors
  {"colors/white" colors/white
   "colors/white-light-transparent" colors/white-light-transparent
   "colors/white-transparent" colors/white-transparent
   "colors/black" colors/black
   "colors/black-transparent" colors/black-transparent
   "colors/gray" colors/gray
   "colors/gray-lighter" colors/gray-lighter
   "colors/blue" colors/blue
   "colors/blue-light" colors/blue-light
   "colors/red" colors/red
   "colors/green" colors/green})

(defn colors-panel []
  [react/view {:style {:margin-bottom 10}}
   [react/view {:style {:flex-direction :row :flex-wrap :wrap}}
    [react/text {:style {:margin-right 5}} "colors ns"]
    (for [[name color] colors-ns-colors]
      [color-panel color name])]
   [color-label]])

(defn check-color [color]
  (let [color-ns (ffirst (filter #(= color (second %)) colors-ns-colors))]
    (cond color-ns
          (symbol color-ns)
          :else
          color)))