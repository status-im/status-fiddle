(ns status-im.ui.screens.main-tabs.styles
  (:require [status-im.utils.platform :as platform])
  (:require-macros [status-im.utils.styles :refer [defnstyle]]))

(def tabs-height (if platform/ios? 52 56))
(def tab-height (dec tabs-height))

(def tabs-container
  {:flex-direction   :row
   :height           tabs-height})

(def tab-container
  {:height          tabs-height
   :justify-content :center
   :align-items     :center})

(defnstyle tab-title [active?]
  {:ios        {:font-size 11}
   :android    {:font-size 12}
   :text-align :center})

(defn tab-icon [active?]
  {})

(def counter-container
  {:position :absolute
   :top      4})

(def counter
  {:margin-left 18})

(def unread-messages-icon
  {:position         :absolute
   :width            20
   :height           20
   :border-radius    20
   :left             18
   :top              10
   :justify-content  :center
   :align-items      :center})

(defn unread-messages-text [large?]
  {:font-size (if large? 10 10.9)})