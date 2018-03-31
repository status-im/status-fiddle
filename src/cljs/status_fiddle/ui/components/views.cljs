(ns status-fiddle.ui.components.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [re-frame.core :as re-frame]
            [status-im.ui.components.styles :as styles]
            [status-fiddle.devices :as devices]))

(defn button [label on-press active?]
  [react/touchable-highlight {:on-press on-press}
   [react/view {:style {:background-color (if (or (nil? active?) active?)
                                            styles/color-light-blue
                                            styles/color-gray)
                        :border-radius    4 :padding-horizontal 10 :padding-vertical 2
                        :margin-bottom    2 :margin-left 10}}
    [react/text {:style {:color :white :font-size 11}}
     label]]])

(defn switch-button [label id active?]
  [button label #(re-frame/dispatch [:set-in [:forms id] (not active?)]) (not active?)])

(defn comp-button [label spurce]
  [react/view {:margin-top 5}
   [button label #(re-frame/dispatch [:update-and-compile-component spurce]) true]])

(defview error-view []
         (letsubs [error [:get :error]]
                  [react/text {:style {:color :red}} error]))

(defn device-chooser []
  [:select
   {:on-change #(let [device (nth devices/devices (js/parseInt (.. % -target -value)))]
                  (re-frame/dispatch [:switch-device device]))}
   (map
     (fn [device]
       ^{:key (:id device)}
       [:option {:value (:id device)} (:phone-name device)])
     devices/devices)])