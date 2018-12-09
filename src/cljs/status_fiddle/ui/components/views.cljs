(ns status-fiddle.ui.components.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [re-frame.core :as re-frame]
            [status-fiddle.devices :as devices]
            [status-im.ui.components.colors :as colors]))

(defn button [label on-press active?]
  [react/touchable-highlight {:on-press on-press}
   [react/view {:style {:background-color (if (or (nil? active?) active?)
                                            colors/blue
                                            colors/gray)
                        :border-radius    4 :padding-horizontal 10 :padding-vertical 2
                        :margin-bottom    2 :margin-left 10}}
    [react/text {:style {:color :white :font-size 11}}
     label]]])

(defn switch-button [label id active?]
  [button
   label
   #(do
      (re-frame/dispatch [:set-in [:forms id] (not active?)])
      (when (= id :extensions)
        (re-frame/dispatch [:compile-and-render :device-dom-target])))
   (not active?)])

(defn comp-button [label source]
  [react/view {:margin-top 5}
   [button label #(re-frame/dispatch [:update-and-compile-component source]) true]])

(defn screen-button [label source]
  [react/view {:margin-top 5}
   [button label #(re-frame/dispatch [:update-and-compile-screen source]) true]])

(defview error-view [target]
  (letsubs [error [:get-in [:error target]]]
    [react/view {:style {:width 350}}
     [react/text {:style {:color :red :flex-wrap :wrap}} error]]))

(defn device-chooser []
  [:select
   {:on-change #(let [device (nth devices/devices (js/parseInt (.. % -target -value)))]
                  (re-frame/dispatch [:switch-device device]))}
   (map
     (fn [device]
       ^{:key (:id device)}
       [:option {:value (:id device)} (:phone-name device)])
     devices/devices)])