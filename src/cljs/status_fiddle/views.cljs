(ns status-fiddle.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-fiddle.gist :as gist]
            [status-fiddle.supported-devices :as devices]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]
            [status-fiddle.icons :as icons]
            [status-fiddle.status-colors :as status-colors]
            [status-fiddle.status-icons :as status-icons]
            [status-im.ui.components.styles :as styles]
            [status-fiddle.svg :as svg]
            [status-fiddle.css :as css]
            [status-fiddle.cheatsheet :as cheatsheet]))

(defn code-mirror []
  (reagent/create-class
    {:reagent-render      (fn []
                            [:div#code-pane])
     :component-did-mount (fn [_]
                            (let [cm (js/CodeMirror (.getElementById js/document "code-pane"))]
                              (re-frame/dispatch [:set-cm cm])
                              (.init js/parinferCodeMirror cm)
                              (.setValue cm @(re-frame/subscribe [:get :source]))
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
  (let [cljs-string @(re-frame/subscribe [:get :source])
        os @(re-frame/subscribe [:get :os])
        compiled-hic (eval-str (empty-state)
                               (str
                                 "(ns cljs.platform)
                                  (def platform \"" os "\")
                                  (def android? " (= os "android") ")"
                                 "(def ios? " (= os "ios") ")"

                                 "(ns cljs.user
                                    (:require reagent.core
                                      [cljs.platform :as platform]
                                      [status-fiddle.react-native-web :as react]
                                      [status-fiddle.icons :as icons]
                                      [status-im.ui.components.styles :as styles]))"
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
        (re-frame/dispatch [:save-the-source])
        (re-frame/dispatch [:delete-error-message]))
      (re-frame/dispatch [:set-error "Hiccup expression is invalid"]))))

(defn button [label on-press active?]
  [react/touchable-highlight {:on-press on-press}
   [react/view {:style {:background-color (if (or (nil? active?) active?)
                                            styles/color-light-blue
                                            styles/color-gray)
                        :border-radius    4 :padding-horizontal 10 :padding-vertical 2
                        :margin-bottom    2 :margin-left 10}}
    [react/text {:style {:color :white :font-size 11}}
     label]]])

(defview buttons [colors icons svg css help]
  (letsubs [url [:get :url]]
    [react/view {:style {:flex-direction :row}}
     [button "Colors" #(re-frame/dispatch [:set-in [:forms :colors] (not colors)]) (not colors)]
     [button "Icons" #(re-frame/dispatch [:set-in [:forms :icons] (not icons)]) (not icons)]
     [button "SVG" #(re-frame/dispatch [:set-in [:forms :svg] (not svg)]) (not svg)]
     [button "CSS" #(re-frame/dispatch [:set-in [:forms :css] (not css)]) (not css)]
     [button "Help" #(re-frame/dispatch [:set-in [:forms :help] (not help)]) (not help)]
     [react/view {:style {:flex 1}}]
     [react/text url]
     [button "Share" #(re-frame/dispatch [:share-source-on-gist])]]))

(defview dom-pane []
  (letsubs [result [:get :result]]
    [react/view {:style {:flex 1}}
     result]))

(defview error-view []
  (letsubs [error [:get :error]]
    [react/text {:style {:color :red}} error]))

(defn device-chooser []
  [:select
   {:on-change #(let [device (nth devices/devices (js/parseInt (.. % -target -value)))]
                  (re-frame/dispatch [:switch-device device]))}
   (map
     (fn [device]
       [:option {:value (:id device)} (:phone-name device)])
     devices/devices)])

(defview main-panel []
  (letsubs [screen-width [:get :screen-width]
            screen-height [:get :screen-height]
            {:keys [colors icons svg css help]} [:get :forms]]
    [react/view {:style {:padding 20}}
     (when colors
       [status-colors/colors-panel])
     (when icons
       [status-icons/icons-panel])
     (when svg
       [svg/svg])
     (when css
       [css/css])
     (when help
       [cheatsheet/cheatsheet])
     [react/view {:style {:flex-direction :row}}
      [react/view {:style {:flex 1}}
       [compiler]
       [buttons colors icons svg css help]
       [react/view {:style {:flex 1}}
        [code-mirror]]]
      [react/view {:style {:margin-left 50}}
       [react/view {:style {:margin-bottom 2}}
        [device-chooser]]
       [react/view {:style {:width        screen-width
                            :height       screen-height
                            :border-width 1
                            :border-color :blue}}
        [dom-pane]]
       [error-view]]]]))
