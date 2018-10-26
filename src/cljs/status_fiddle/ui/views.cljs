(ns status-fiddle.ui.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-fiddle.ui.panels.status-colors :as status-colors]
            [status-fiddle.ui.panels.status-icons :as status-icons]
            [status-fiddle.ui.panels.svg :as panels.svg]
            [status-fiddle.ui.panels.css :as css]
            [status-fiddle.ui.panels.cheatsheet :as cheatsheet]
            [status-im.ui.components.react :as react]
            [status-fiddle.ui.code-mirror.views :as code-mirror]
            [status-fiddle.ui.components.views :as ui]
            [status-fiddle.ui.panels.components :as panels.components]
            [status-fiddle.ui.panels.screens :as panels.screens]))

(defview buttons []
  (letsubs [url [:get :url]
            {:keys [colors icons svg css help components screens extensions]} [:get :forms]]
    [react/view {:style {:flex-direction :row}}
     [ui/switch-button "Colors" :colors colors]
     [ui/switch-button "Icons" :icons  icons]
     [ui/switch-button "SVG" :svg svg]
     [ui/switch-button "CSS" :css css]
     [react/text {:style {:margin-left 10}} "|"]
     [ui/switch-button "Components" :components components]
     [ui/switch-button "Screens" :screens screens]
     [ui/switch-button "Extensions" :extensions extensions]
     [react/text {:style {:margin-left 10}} "|"]
     [ui/switch-button "Help" :help help]
     [react/view {:style {:flex 1}}]]))
     ;TODO they closed anonymous gists :(
     ;[react/text url]
     ;[ui/button "Share" #(re-frame/dispatch [:share-source-on-gist])]]))

(defview panels []
  (letsubs [{:keys [colors icons svg css help]} [:get :forms]]
    [react/view
     (when colors [status-colors/colors-panel])
     (when icons [status-icons/icons-panel])
     (when svg [panels.svg/svg])
     (when css [css/css])
     (when help [cheatsheet/cheatsheet])]))

(defview device-dom-target []
  (letsubs [screen-width [:get :screen-width]
            screen-height [:get :screen-height]]
    [react/view {:style {:width        screen-width
                         :height       screen-height
                         :border-width 1
                         :border-color :blue}}
     [:div#device-dom-target]]))

(defview main-panel []
  (letsubs [{:keys [extensions]} [:get :forms]]
    [react/view {:style {:padding 20}}
     ;top panels
     [panels]
     ;;MAIN VIEW
     [react/view {:style {:flex-direction :row}}
      [react/view {:style {:flex 1}}
       ;buttons
       [buttons]
       ;code editor
       [react/view {:style {:flex 1}}
        (when extensions
         [react/text "Extensions mode"])
        [code-mirror/code-mirror :device-code-mirror :device-dom-target]
        [panels.components/components-panel]
        [panels.screens/screens-panel]]]
      [react/view {:style {:margin-left 20}}
       [react/view {:style {:margin-bottom 2}}
        ;device chooser
        [ui/device-chooser]]
       ;device
       [device-dom-target]
       ;compile error
       [ui/error-view :device-dom-target]]]]))
