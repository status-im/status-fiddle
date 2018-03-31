(ns status-fiddle.ui.panels.cheatsheet
  (:require [status-im.ui.components.react :as react]))

(def ch-str
  "# Choose the main axis | :flex-direction | :column or :row

# Align the main axis | :justify-content | :flex-start or :flex-end or :center or :space-around or :space-between

# Align the cross axis | :align-items | :flex-start or :flex-end or :center or :stretch

# Align individual elements | :align-self | :flex-start or :flex-end or :center or :stretch or :auto

# Give it a wrap | :flex-wrap | :wrap or :nowrap

# Define relative fluidity of an element | :flex | number (e.g. 1, 2))

# Padding | :padding :padding-left :padding-right :padding-top :padding-bottom :padding-horizontal :padding-vertical | number

# Margin | :margin :margin-left :margin-right :margin-top :margin-bottom :margin-horizontal :margin-vertical | number")


(defn cheatsheet []
  [react/view {:style {:margin-vertical 20 :border-width 1 :border-color :gray :padding 20}}
   [react/text {:style {:font-family "COURIER" :margin-bottom 10}}
    ch-str]
   [:a {:href "https://github.com/vhpoet/react-native-styling-cheat-sheet" :target "_blank"} "more"]])