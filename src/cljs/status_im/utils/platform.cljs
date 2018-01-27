(ns status-im.utils.platform
  (:require [re-frame.core :as re-frame]))

(def platform @(re-frame/subscribe [:current-os]))

(def android? (= platform "android"))
(def ios? (= platform "ios"))

(def platform-specific
  (cond
    android? {}
    ios? {}
    :else {}))