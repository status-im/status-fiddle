(ns status-fiddle.ui.panels.status-icons
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.icons.vector-icons :as icons]))

(defn icon-panel [icon]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:show-icon icon])}
   [icons/icon icon]])

(defview icon-label []
  (letsubs [icon [:get :icon]]
    (when icon
      [react/view {:style {:flex-direction :row :align-items :center}}
       [icons/icon icon]
       [react/text {:style {:margin-left 20}} (str "[icons/icon " icon "]")]])))

(defn icons-panel []
  [react/view {:style {:margin-bottom 10}}
   [react/view {:style {:flex-direction :row :flex-wrap :wrap}}
    (for [icon (keys icons/icons)]
      [icon-panel icon])]
   [icon-label]])