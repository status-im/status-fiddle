(ns status-fiddle.react-native-web
  (:require [reagent.core :as reagent]
            [react-native-web :refer [View]]))

#_(defonce react-native-web
    (cond (exists? js/ReactNative) js/ReactNative
          (exists? js/require) (or (js/require "react-native-web")
                                   (throw (js/Error. "require('react-native-web') failed")))
          :else (throw (js/Error. "react-native-web is missing")))

    (defn get-react-property [name]
      (aget react-native-web name))

    (defn adapt-class [class]
      (when class
        (reagent/adapt-react-class class)))

    (defn get-class [name]
      (adapt-class (get-react-property name))))

(def view View)
;(def text (get-class "Text"))
;(def image (get-class "Image"))
;(def touchable-highlight (get-class "TouchableOpacity"))
;(def scroll-view (get-class "ScrollView"))
;(def text-input (get-class "TextInput"))