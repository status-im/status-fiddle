(ns status-fiddle.react-native-web
  (:require [reagent.core :as reagent]))

(defn get-react-property [name]
  (goog.object/get js/ReactNativeWeb name))

(defn adapt-class [class]
  (when class
    (reagent/adapt-react-class class)))

(defn get-class [name]
  (adapt-class (get-react-property name)))

(def view (get-class "View"))
(def text (get-class "Text"))
(def image (get-class "Image"))
(def touchable-highlight (get-class "TouchableOpacity"))
(def scroll-view (get-class "ScrollView"))
(def text-input (get-class "TextInput"))
