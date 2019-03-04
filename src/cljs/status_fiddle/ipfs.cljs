(ns status-fiddle.ipfs
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [status-fiddle.utils :as utils]))

(defn set-url [url]
  (js/history.pushState nil nil url))

(defn save [content extension?]
  "Saves content to ipfs"
  (let [form-data (doto
                   (js/FormData.)
                   (.append "extension.edn" content))]
    (println "ext" extension?)
    (ajax/POST
      "https://ipfs.infura.io:5001/api/v0/add"
      {:body            form-data
       :response-format :json
       :keywords?       true
       :handler         (fn [{:keys [Hash]}]
                          (re-frame/dispatch [:set-in [:publish :in-progress?] false])
                          (when Hash
                            (re-frame/dispatch [:set-in [:publish :url] {:extension? extension?
                                                                         :hash Hash}])
                            (cond-> (utils/current-url-without-anchor)
                                    true
                                    (utils/assoc-anchor "ipfs" Hash)
                                    extension?
                                    (utils/assoc-anchor "ext" "true")
                                    true
                                    str
                                    true
                                    set-url)))
       :error-handler   (fn [x]
                          (re-frame/dispatch [:set-in [:publish :in-progress?] false])
                          (str "ERROR" (js/console.log x)))})))


(defn load [hash handler]
  (ajax/GET (str "https://ipfs.infura.io/ipfs/" hash)
            {:with-credentials? false
             :handler           (fn [response-text]
                                  (println "rep" response-text)
                                  (handler response-text))}))