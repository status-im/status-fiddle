(ns status-fiddle.ui.panels.svg
  (:require [hickory.core :as hickory]
            [reagent.core :as reagent]
            [clojure.string :as string]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.icons.vector-icons :as icons]))

(defn svg []
  (let [svg-hiccup (reagent/atom "")]
    (fn []
      [react/view {:style {:flex-direction :row :align-items :center :margin-vertical 20}}
       [react/text-input {:style       {:flex 1 :height 200 :border-color :gray :border-width 1}
                          :multiline   true :textAlignVertical :top
                          :on-change   (fn [e]
                                        (let [native-event (.-nativeEvent e)
                                              text         (.-text native-event)]
                                          (reset! svg-hiccup (map hickory/as-hiccup (hickory/parse-fragment text)))))
                          :placeholder "Paste svg xml here"}]
       [icons/icon :icons/forward]
       [react/text-input {:style     {:flex 1 :height 200 :border-color :gray :border-width 1}
                          :multiline true :textAlignVertical :top
                          :editable  false
                          :value     (string/replace (str (first @svg-hiccup)) #"viewbox" "viewBox")}]])))