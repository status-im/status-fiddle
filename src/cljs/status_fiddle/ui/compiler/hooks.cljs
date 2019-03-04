(ns status-fiddle.ui.compiler.hooks
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [pluto.reader.hooks :as hooks]
            [status-im.ui.components.toolbar.view :as toolbar]
            [status-im.ui.components.colors :as colors]
            [status-im.ui.components.toolbar.actions :as actions]
            [status-im.ui.components.icons.vector-icons :as icons]
            [re-frame.core :as re-frame]))

(defn message-container [preview outgoing]
  [react/view
   [react/view {:style {:margin-top     20
                        :flex-direction (if outgoing :row-reverse :row)
                        ;:width          230
                        :flex           1
                        :align-self     (if outgoing :flex-end :flex-start)
                        :align-items    (if outgoing :flex-end :flex-start)}}
    [react/view {:style (merge
                         (if outgoing
                           {:margin-left 64}
                           {:margin-right 64})
                         {:flex-direction :column
                          ;:width          230
                          :flex           1
                          :padding-left   8
                          :padding-right  8
                          :align-items    (if outgoing :flex-end :flex-start)})}
     [react/view {:style {:flex-direction (if outgoing :row-reverse :row)}}
      [react/view {:style {
                           :flex               1
                           :padding-vertical   6
                           :padding-horizontal 12
                           :border-radius      8
                           :padding-top        12
                           :padding-bottom     10
                           :flex-wrap          :wrap
                           :background-color   (if outgoing colors/blue colors/blue-light)}}
       preview]]]]])

(def settings-hook
  "Hook for extensions"
  {:properties
   {:label     :string
    :view      :view
    :on-click? :event}
   :hook
   (reify hooks/Hook
     (hook-in [_ id env {:keys [label view on-click]} {:keys [db]}]
       [react/view {:style {:flex 1}}
        [toolbar/toolbar {:background-color colors/blue}
         [toolbar/nav-button (actions/back-white #())]
         [toolbar/content-title {:color :white :font-weight :bold} label]]
        [view]])
     (unhook [_ id env _ {:keys [db]}]
       nil))})

(def input-container
  {:flex-direction :row
   :align-items    :flex-end
   :padding-left   14})

(def send-message-container
  {:background-color colors/blue
   :width            30
   :height           30
   :border-radius    15
   :margin           10
   :padding          4
   :margin-left      8
   :margin-bottom    11
   :transform        [{:rotate "90deg"}]})

(def send-message-icon
  {:height 22
   :width  22})

(def input-root
  {:padding-top    8
   :padding-bottom 8
   :flex           1})

(def input-animated
  {:align-items    :center
   :flex-direction :row
   :flex-grow      1
   :min-height     36})

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defview chat-view [preview parameters command-id]
  (letsubs [{:keys [messages params suggestion-id]} [:get :extension-props]]
    [react/view {:style {:flex 1 :background-color colors/white}}
     [react/scroll-view {:style {:flex 1}}
      (for [message messages]
        ^{:key (str message (rand-str 10))}
        [react/view
         [message-container (when preview (preview (merge {:outgoing false} message))) false]
         [message-container (when preview (preview (merge {:outgoing true} message))) true]])]
     (when-let [suggestion (some #(when (= suggestion-id (:id %)) (:suggestions %)) parameters)]
       [suggestion])
     [react/view {:style input-container}
      [react/view {:style input-root}
       [react/view {:style input-animated}
        [react/text {:style {:border-width 1 :border-color :red}} (str "/" (name command-id) " ")]
        (for [{:keys [placeholder id]} parameters]
          [react/text-input {:placeholder    placeholder
                             :on-change-text #(re-frame/dispatch [:set-in [:extension-props :params id] %])
                             :on-focus       #(re-frame/dispatch [:set-in [:extension-props :suggestion-id] id])
                             :style          {:margin-right 5 :width 50}}])]]
      [react/touchable-highlight {:on-press #(do
                                               (re-frame/dispatch [:set-in [:extension-props :suggestion-id] nil])
                                               (re-frame/dispatch [:set-in [:extension-props :messages] (conj messages {:content {:params params}})]))}
       [react/view {:style send-message-container}
        [icons/icon :main-icons/arrow-up {:container-style send-message-icon
                                          :color           :white}]]]]]))

(def command-hook
  "Hook for extensions"
  {:properties
   {:description?   :string
    :scope          #{:personal-chats :public-chats :group-chats}
    :short-preview? :view
    :preview?       :view
    :on-send?       :event
    :on-receive?    :event
    :on-send-sync?  :event
    :parameters?    [{:id           :keyword
                      :type         {:one-of #{:text :phone :password :number}}
                      :placeholder  :string
                      :suggestions? :view}]}
   :hook
   (reify hooks/Hook
     (hook-in [_ id {extension-id :id} {:keys [description scope parameters preview short-preview
                                               on-send on-receive on-send-sync]} cofx]
       [chat-view preview parameters id])
     (unhook [_ id _ {:keys [scope]} {:keys [db] :as cofx}]
       nil))})
