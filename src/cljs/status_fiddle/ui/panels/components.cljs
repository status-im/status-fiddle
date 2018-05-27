(ns status-fiddle.ui.panels.components
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [status-fiddle.templates :as templates]
            [status-fiddle.ui.code-mirror.views :as code-mirror]
            [status-fiddle.ui.components.views :as ui]))

(defview components-panel []
  (letsubs [{:keys [components]} [:get :forms]]
    (when components
      [react/view {:margin-top 40}
              [react/view {:style {:flex-direction :row}}
               [react/view
                [react/scroll-view
                 [react/view
                  [ui/comp-button "Button" templates/button]
                  [ui/comp-button "Button 2" templates/button2]
                  [ui/comp-button "Action button" templates/action-button]
                  [ui/comp-button "Bottom buttons" templates/bottom-buttons]
                  [ui/comp-button "Bottom buttons 2" templates/bottom-buttons2]
                  [ui/comp-button "Input" templates/input]
                  [ui/comp-button "Contact" templates/contact]
                  [ui/comp-button "Toolbar" templates/toolbar]]]]
               [react/view {:style {:flex 1 :margin-horizontal 10}}
                [code-mirror/code-mirror :component-code-mirror :component-dom-target]]
               [react/view
                [react/view {:style {:width        300 :height 300
                                     :border-width 1 :border-color :gray}}
                 [:div#component-dom-target]]
                [ui/error-view :component-dom-target]]]])))