(ns status-fiddle.status-colors
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-fiddle.react-native-web :as react]
            [status-im.ui.components.styles :as styles]
            [re-frame.core :as re-frame]
            [status-im.ui.components.colors :as colors]))

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
   "colors/white-lighter-transparent" colors/white-lighter-transparent
   "colors/black" colors/black
   "colors/black-transparent" colors/black-transparent
   "colors/gray" colors/gray
   "colors/gray-light" colors/gray-light
   "colors/gray-lighter" colors/gray-lighter
   "colors/blue" colors/blue
   "colors/red" colors/red
   "colors/text-light-gray" colors/text-light-gray})

(def style-ns-colors
  {"styles/color-blue" styles/color-blue
   "styles/color-blue2" styles/color-blue2
   "styles/color-blue3" styles/color-blue3
   "styles/color-blue4" styles/color-blue4
   "styles/color-blue4-faded" styles/color-blue4-faded
   "styles/color-blue4-transparent" styles/color-blue4-transparent
   "styles/color-blue6" styles/color-blue6
   "styles/color-blue-transparent" styles/color-blue-transparent
   "styles/color-black" styles/color-black
   "styles/color-purple" styles/color-purple
   "styles/color-gray-transparent-light" styles/color-gray-transparent-light
   "styles/color-gray-transparent-medium-light" styles/color-gray-transparent-medium-light
   "styles/color-gray-transparent" styles/color-gray-transparent
   "styles/color-gray4-transparent" styles/color-gray4-transparent
   "styles/color-gray10-transparent" styles/color-gray10-transparent
   "styles/color-gray" styles/color-gray
   "styles/color-gray2" styles/color-gray2
   "styles/color-gray3" styles/color-gray3
   "styles/color-gray4" styles/color-gray4
   "styles/color-gray5" styles/color-gray5
   "styles/color-gray6" styles/color-gray6
   "styles/color-gray7" styles/color-gray7
   "styles/color-gray9" styles/color-gray9
   "styles/color-gray10" styles/color-gray10
   "styles/color-gray11" styles/color-gray11
   "styles/color-dark" styles/color-dark
   "styles/color-steel" styles/color-steel
   "styles/color-white" styles/color-white
   "styles/color-white-transparent" styles/color-white-transparent
   "styles/color-white-transparent-1" styles/color-white-transparent-1
   "styles/color-white-transparent-2" styles/color-white-transparent-2
   "styles/color-white-transparent-3" styles/color-white-transparent-3
   "styles/color-white-transparent-4" styles/color-white-transparent-4
   "styles/color-white-transparent-5" styles/color-white-transparent-5
   "styles/color-white-transparent-6" styles/color-white-transparent-6
   "styles/color-light-blue" styles/color-light-blue
   "styles/color-light-blue-transparent" styles/color-light-blue-transparent
   "styles/color-light-blue6" styles/color-light-blue6
   "styles/color-dark-blue-1" styles/color-dark-blue-1
   "styles/color-dark-blue-2" styles/color-dark-blue-2
   "styles/color-dark-blue-3" styles/color-dark-blue-3
   "styles/color-dark-blue-4" styles/color-dark-blue-4
   "styles/color-light-gray" styles/color-light-gray
   "styles/color-light-gray2" styles/color-light-gray2
   "styles/color-light-gray3" styles/color-light-gray3
   "styles/color-light-gray4" styles/color-light-gray4
   "styles/color-light-gray5" styles/color-light-gray5
   "styles/color-red" styles/color-red
   "styles/color-red-2" styles/color-red-2
   "styles/color-red-3" styles/color-red-3
   "styles/color-red-4" styles/color-red-4
   "styles/color-light-red" styles/color-light-red
   "styles/color-green-1" styles/color-green-1
   "styles/color-green-2" styles/color-green-2
   "styles/color-green-3" styles/color-green-3
   "styles/color-green-3-light" styles/color-green-3-light
   "styles/color-green-4" styles/color-green-4
   "styles/color-cyan" styles/color-cyan
   "styles/color-separator" styles/color-separator
   "styles/selected-message-color" styles/selected-message-color
   "styles/separator-color" styles/separator-color})

(defn colors-panel []
  [react/view {:style {:margin-bottom 10}}
   [react/view {:style {:flex-direction :row :flex-wrap :wrap}}
    [react/text {:style {:margin-right 5}} "colors ns"]
    (for [[name color] colors-ns-colors]
      [color-panel color name])]
   [react/view {:style {:flex-direction :row :flex-wrap :wrap :margin-top 3}}
    [react/text {:style {:margin-right 5}} "styles ns"]
    (for [[name color] style-ns-colors]
      [color-panel color name])]
   [color-label]])

(defn check-color [color]
  (let [color-ns (ffirst (filter #(= color (second %)) colors-ns-colors))
        style-ns (ffirst (filter #(= color (second %)) style-ns-colors))]
    (cond color-ns
          (symbol color-ns)
          style-ns
          (symbol style-ns)
          :else
          color)))