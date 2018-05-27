(ns status-fiddle.ui.panels.screens
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [status-fiddle.templates :as templates]
            [status-fiddle.ui.code-mirror.views :as code-mirror]
            [status-fiddle.ui.components.views :as ui]))

(defview screens-panel []
  (letsubs [{:keys [screens]} [:get :forms]]
    (when screens
      [react/view {:margin-top 40}
       [react/view {:style {:flex-direction :row :max-height 667}}
        [react/view
         [react/scroll-view
          [react/view
           [ui/screen-button "Intro Screen" templates/screens-intro]
           [ui/screen-button "Profile Screen" templates/screens-profile]]]]
        [react/scroll-view
         [react/view {:style {:flex 1 :margin-horizontal 10}}
          [code-mirror/code-mirror :screen-code-mirror :screen-dom-target]]]
        [react/view
         [react/view {:style {:width        375
                              :height       667
                              :border-width 1
                              :border-color :gray}}
          [:div#screen-dom-target]]
         [ui/error-view :screen-dom-target]]]])))