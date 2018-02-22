(ns status-fiddle.react-native-web
  (:require [reagent.core :as reagent]
            [clojure.string :as string]))

(defn get-react-property [name]
  (goog.object/get js/ReactNativeWeb name))

(defn adapt-class [class]
  (when class
    (reagent/adapt-react-class class)))

(defn get-class [name]
  (adapt-class (get-react-property name)))

(def view (get-class "View"))
(def text-class (get-class "Text"))
(def image (get-class "Image"))
(def touchable-highlight (get-class "TouchableOpacity"))
(def scroll-view (get-class "ScrollView"))
(def text-input (get-class "TextInput"))

(defn text
  ([t]
   (reagent/as-element [text-class t]))
  ([{:keys [uppercase?] :as opts} t & ts]
   (reagent/as-element
     (let [ts (cond->> (conj ts t)
                       uppercase? (map #(when % (string/upper-case %))))]
       (vec (concat
              [text-class opts]
              ts))))))