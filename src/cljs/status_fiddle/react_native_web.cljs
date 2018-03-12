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
(def activity-indicator (get-class "ActivityIndicator"))
(def art (get-class "ART"))
(def button (get-class "Button"))
(def check-box (get-class "CheckBox"))
(def flatList (get-class "FlatList"))
(def image-background (get-class "ImageBackground"))
(def keyboard-avoiding-view (get-class "KeyboardAvoidingView"))
(def list-view (get-class "ListView"))
(def modal (get-class "Modal"))
(def picker (get-class "Picker"))
(def progress-bar (get-class "ProgressBar"))
(def refresh-control (get-class "RefreshControl"))
(def safe-area-view (get-class "SafeAreaView"))
(def section-list (get-class "SectionList"))
(def slider (get-class "Slider"))
(def status-bar (get-class "StatusBar"))
(def switch (get-class "Switch"))
(def touchable (get-class "Touchable"))
(def touchable-native-feedback (get-class "TouchableNativeFeedback"))
(def touchable-opacity (get-class "TouchableOpacity"))
(def touchable-without-feedback (get-class "TouchableWithoutFeedback"))
(def virtualized-list (get-class "VirtualizedList"))

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