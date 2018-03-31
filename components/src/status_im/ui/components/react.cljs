(ns status-im.ui.components.react
  (:require [reagent.core :as reagent]
            [clojure.string :as string]
            [status-im.react-native.js-dependencies :as js-dependencies]
            [status-im.utils.platform :as platform]
            [status-im.i18n :as i18n]
            [status-im.ui.components.styles :as styles]))

(defn adapt-class [class]
  (when class
    (reagent/adapt-react-class class)))

(defn get-web-class [name]
  (adapt-class (goog.object/get js/ReactNativeWeb name)))

(def view-class (get-web-class "View"))
(def text-class (get-web-class "Text"))
(def image (get-web-class "Image"))
(def touchable-highlight (get-web-class "TouchableOpacity"))
(def scroll-view (get-web-class "ScrollView"))
(def text-input-class (get-web-class "TextInput"))

(defn view [& props]
  (into []
    (concat
      [view-class]
      (cond-> (into [] props)
              (and (map? (first props)) (not (contains? (first props) :style)))
              (assoc 0 {:style (first props)})))))

(defn add-font-style [style-key {:keys [font] :as opts :or {font :default}}]
  (let [font (get-in platform/platform-specific [:fonts (keyword font)])
        style (get opts style-key)]
    (-> opts
        (dissoc :font)
        (assoc style-key (merge style font)))))

(defn transform-to-uppercase [{:keys [uppercase? force-uppercase?] :as opts} ts]
  (if (or force-uppercase? (and uppercase? platform/android?))
    (vec (map string/upper-case ts))
    ts))

(defn text
  ([t]
   [text-class t])
  ([opts t & ts]
   (->> (conj ts t)
        (transform-to-uppercase opts)
        (concat [text-class (add-font-style :style opts)])
        (vec))))

(defn text-input [{:keys [font style] :as opts
                   :or   {font :default}} text]
  (let [font (get-in platform/platform-specific [:fonts (keyword font)])]
    [text-input-class (merge
                        {:underline-color-android :transparent
                         :placeholder-text-color  styles/text2-color
                         :placeholder             (i18n/label :t/type-a-message)
                         :value                   text}
                        (-> opts
                            (dissoc :font)
                            (assoc :style (merge style font))))]))

;;;;; ============= status-react =============================

(defn get-react-property [name]
  #js {})

(defn get-class [name]
  (adapt-class (get-react-property name)))

(def native-modules (.-NativeModules js-dependencies/react-native))
(def device-event-emitter (.-DeviceEventEmitter js-dependencies/react-native))
(def dismiss-keyboard! js-dependencies/dismiss-keyboard)
(def back-handler (get-react-property "BackHandler"))

(def splash-screen (.-SplashScreen native-modules))

;; React Components

(def app-registry (get-react-property "AppRegistry"))
(def app-state (get-react-property "AppState"))
(def net-info (get-react-property "NetInfo"))

(def safe-area-view (get-class "SafeAreaView"))

(def status-bar (get-class "StatusBar"))

(def web-view (get-class "WebView"))
(def keyboard-avoiding-view-class (get-class "KeyboardAvoidingView"))

(def refresh-control (get-class "RefreshControl"))

(def switch (get-class "Switch"))
(def check-box (get-class "CheckBox"))

(def touchable-highlight-class (get-class "TouchableHighlight"))
(def touchable-opacity (get-class "TouchableOpacity"))
(def activity-indicator (get-class "ActivityIndicator"))

(def modal (get-class "Modal"))

(def pan-responder (.-PanResponder js-dependencies/react-native))
(def animated (.-Animated js-dependencies/react-native))
(def animated-view (reagent/adapt-react-class (.-View animated)))
(def animated-text (reagent/adapt-react-class (.-Text animated)))

(def dimensions (.-Dimensions js-dependencies/react-native))
(def keyboard (.-Keyboard js-dependencies/react-native))
(def linking (.-Linking js-dependencies/react-native))

(def slider (get-class "Slider"))
;; Accessor methods for React Components

(defn icon
  ([n] (icon n styles/icon-default))
  ([n style]
   [image {:source     {:uri (keyword (str "icon_" (name n)))}
           :resizeMode "contain"
           :style      style}]))

(defn get-dimensions [name]
  (js->clj (.get dimensions name) :keywordize-keys true))

(defn list-item [component]
  (reagent/as-element component))

;; Image picker

(def image-picker-class js-dependencies/image-crop-picker)

(defn show-access-error [o])

(defn show-image-picker [images-fn]
  (let [image-picker (.-default image-picker-class)]
    (-> image-picker
        (.openPicker (clj->js {:multiple false}))
        (.then images-fn)
        (.catch show-access-error))))

;; Clipboard

(def sharing
  (.-Share js-dependencies/react-native))

(defn copy-to-clipboard [text]
  (.setString (.-Clipboard js-dependencies/react-native) text))

(defn get-from-clipboard [clbk]
  (let [clipboard-contents (.getString (.-Clipboard js-dependencies/react-native))]
    (.then clipboard-contents #(clbk %))))

;; HTTP Bridge

(def http-bridge js-dependencies/http-bridge)

;; KeyboardAvoidingView

(defn keyboard-avoiding-view [props & children]
  (let [view-element (if platform/ios?
                       [keyboard-avoiding-view-class (merge {:behavior :padding} props)]
                       [view props])]
    (vec (concat view-element children))))