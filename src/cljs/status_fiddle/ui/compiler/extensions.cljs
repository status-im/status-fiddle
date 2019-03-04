(ns status-fiddle.ui.compiler.extensions
  (:require [status-im.ui.components.react :as react]
            [status-im.ui.components.colors :as colors]
            [pluto.reader :as reader]
            [status-im.ui.components.button.view :as button]
            [re-frame.core :as re-frame]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.checkbox.view :as checkbox]
            [status-im.ui.components.icons.vector-icons :as icons]
            [clojure.string :as string]
            [status-fiddle.ui.compiler.hooks :as hooks]
            [ajax.core :as ajax]))

(re-frame/reg-event-fx
 :extensions/identity-event
 (fn [_ [_ _ {:keys [cb]}]]
   {:dispatch  (cb {})}))

(re-frame/reg-fx
 ::alert
 (fn [value] (js/alert value)))

(re-frame/reg-event-fx
 :alert
 (fn [_ [_ _ {:keys [value]}]]
   {::alert value}))

(re-frame/reg-fx
 ::log
 (fn [value] (js/console.log value)))

(re-frame/reg-event-fx
 :log
 (fn [_ [_ _ {:keys [value]}]]
   {::log value}))

(re-frame/reg-fx
 ::schedule-start
 (fn [{:keys [interval on-created on-result]}]
   (let [id (js/setInterval #(re-frame/dispatch (on-result {})) interval)]
     (re-frame/dispatch (on-created {:value id})))))

(re-frame/reg-event-fx
 :extensions/schedule-start
 (fn [_ [_ _ m]]
   {::schedule-start m}))

(re-frame/reg-fx
 ::schedule-cancel
 (fn [{:keys [value]}]
   (js/clearInterval value)))

(re-frame/reg-event-fx
 :extensions/schedule-cancel
 (fn [_ [_ _ m]]
   {::schedule-cancel m}))

(re-frame/reg-sub
 :extensions/identity
 (fn [_ [_ _ {:keys [value]}]]
   value))

(defn get-token-for [network all-tokens token]
  {:decimals 18
   :address  "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"})

(re-frame/reg-sub
 :extensions.wallet/balance
 (fn [_ [_ _ {token :token}]]
   {:value        100
    :value-in-wei 10000000000000}))

(re-frame/reg-sub
 :extensions.wallet/token
 (fn [_ [_ _ {token :token amount :amount}]]
   {:amount 100}))

(re-frame/reg-sub
 :extensions.wallet/tokens
 (fn [_ [_ _ {filter-vector :filter}]]
   [{:name "Test"}]))

(re-frame/reg-sub
 :store/get
 (fn [db [_ {id :hook-id} {:keys [key]}]]
   (get-in db [:extensions/store id key])))

(defn- empty-value? [o]
  (cond
    (seqable? o) (empty? o)
    :else (nil? o)))

(defn put-or-dissoc [db id key value]
  (if (empty-value? value)
    (update-in db [:extensions/store id] dissoc key)
    (assoc-in db [:extensions/store id key] value)))

(re-frame/reg-event-fx
 :store/put
 (fn [{:keys [db]} [_ {id :hook-id} {:keys [key value]}]]
   {:db (put-or-dissoc db id key value)}))

(re-frame/reg-event-fx
 :store/puts
 (fn [{:keys [db]} [_ {id :id} {:keys [value]}]]
   {:db (reduce #(put-or-dissoc %1 id (:key %2) (:value %2)) db value)}))

(defn- append [acc k v]
  (let [o (get acc k)]
    (assoc acc k (conj (if (vector? o) o (vector o)) v))))

(re-frame/reg-event-fx
 :store/append
 (fn [{:keys [db]} [_ {id :id} {:keys [key value]}]]
   {:db (update-in db [:extensions/store id] append key value)}))

(re-frame/reg-event-fx
 :store/clear
 (fn [{:keys [db]} [_ {id :id} {:keys [key]}]]
   {:db (update-in db [:extensions/store id] dissoc key)}))

(defn- json? [res]
  (when-let [type (get-in res [:headers "content-type"])]
    (string/starts-with? type "application/json")))

(defn- parse-json [o]
  (when o
    (js->clj (js/JSON.parse o) :keywordize-keys true)))

(re-frame/reg-fx
 ::json-parse
 (fn [{:keys [value on-result]}]
   (re-frame/dispatch (on-result {:value (parse-json value)}))))

(re-frame/reg-event-fx
 :extensions/json-parse
 (fn [_ [_ _ m]]
   {::json-parse m}))

(re-frame/reg-fx
 ::json-stringify
 (fn [value on-result]
   (re-frame/dispatch (on-result {:value (js/JSON.stringify (clj->js value))}))))

(re-frame/reg-event-fx
 :extensions/json-stringify
 (fn [_ [_ _ {:keys [value]}]]
   {::json-stringify value}))

(defn- parse-result [result on-success]
  (let [res (try (parse-json result) (catch js/Error _))]
    (on-success {:body (or res result)})))

(re-frame/reg-event-fx
 :http/get
 (fn [_ [_ _ {:keys [url on-success on-failure timeout]}]]
   (ajax/GET url {:with-credentials? false
                  :response-format :text
                  :handler #(when on-success (re-frame/dispatch (parse-result (str %) on-success)))
                  :error-handler on-failure})
   nil))

(re-frame/reg-event-fx
 :ipfs/cat
 (fn [_ [_ _ {:keys [hash on-success on-failure]}]]
   nil #_{:http-raw-get (merge {:url (str constants/ipfs-cat-url hash)
                                :success-event-creator
                                     (fn [{:keys [status body]}]
                                       (if (= 200 status)
                                         (on-success {:value body})
                                         (when on-failure
                                           (on-failure {:value status}))))}
                               (when on-failure
                                 {:failure-event-creator on-failure})
                               {:timeout-ms 5000})}))

(defn- parse-ipfs-add-response [res]
  (let [{:keys [Name Hash Size]} (parse-json res)]
    {:name Name
     :hash Hash
     :size Size}))

(re-frame/reg-event-fx
 :ipfs/add
 (fn [_ [_ _ {:keys [value on-success on-failure]}]]
   nil
   #_(let [formdata (doto
                     (js/FormData.
                      (.append constants/ipfs-add-param-name value)))]
       {:http-raw-post (merge {:url  constants/ipfs-add-url
                               :body formdata
                               :success-event-creator
                                     (fn [{:keys [status body]}]
                                       (if (= 200 status)
                                         (on-success {:value (parse-ipfs-add-response body)})
                                         (when on-failure
                                           (on-failure {:value status}))))}
                              (when on-failure
                                {:failure-event-creator on-failure})
                              {:timeout-ms 5000})})))

(re-frame/reg-event-fx
 :http/post
 (fn [_ [_ _ {:keys [url body on-success on-failure timeout]}]]
   nil
   #_{:http-raw-post (merge {:url  url
                             :body (clj->js body)
                             :success-event-creator #(parse-result % on-success)}
                            (when on-failure
                              {:failure-event-creator on-failure})
                            (when timeout
                              {:timeout-ms timeout}))}))

(re-frame/reg-event-fx
 :extensions.chat.command/set-parameter
 (fn [_ [_ _ {:keys [value]}]]
   nil #_{:dispatch [:chat.ui/set-command-parameter value]}))

(re-frame/reg-event-fx
 :extensions.chat.command/set-custom-parameter
 (fn [{{:keys [current-chat-id] :as db} :db} [_ _ {:keys [key value]}]]
   nil #_{:db (assoc-in db [:chats current-chat-id :custom-params key] value)}))

(re-frame/reg-event-fx
 :extensions.chat.command/set-parameter-with-custom-params
 (fn [{{:keys [current-chat-id] :as db} :db} [_ _ {:keys [value params]}]]
   {:db (-> db
            (update-in  [:extension-props :params] merge params)
            (assoc-in  [:extension-props :suggestion-id] nil))}))

(re-frame/reg-event-fx
 :extensions.chat.command/send-plain-text-message
 (fn [_ [_ _ {:keys [value]}]]
   nil #_{:dispatch [:chat/send-plain-text-message value]}))

(re-frame/reg-event-fx
 :extensions.chat.command/send-message
 (fn [{{:keys [current-chat-id] :as db} :db :as cofx} [_ {:keys [hook-id]} {:keys [params]}]]
   nil #_(when hook-id
           (when-let [command (last (first (filter #(= (ffirst %) (name hook-id)) (:id->command db))))]
             (commands-sending/send cofx current-chat-id command params)))))

(re-frame/reg-event-fx
 :extensions/show-selection-screen
 (fn [cofx [_ _ {:keys [on-select] :as params}]]
   nil #_(navigation/navigate-to-cofx cofx
                                      :selection-modal-screen
                                      (assoc params :on-select #(do
                                                                  (re-frame/dispatch [:navigate-back])
                                                                  (re-frame/dispatch (on-select %)))))))

(defn operation->fn [k]
  (case k
    :plus   +
    :minus  -
    :times  *
    :divide /))

(re-frame/reg-fx
 ::arithmetic
 (fn [{:keys [operation values on-result]}]
   (re-frame/dispatch (on-result {:value (apply (operation->fn operation) values)}))))

(re-frame/reg-event-fx
 :extensions/arithmetic
 (fn [_ [_ _ m]]
   {::arithmetic m}))

(defn button [{:keys [on-click enabled disabled] :as m} label]
  [button/secondary-button (merge {:disabled? (or (when (contains? m :enabled) (or (nil? enabled) (false? enabled))) disabled)}
                                  (when on-click {:on-press #(re-frame/dispatch (on-click {}))})) label])

(defn input [{:keys [keyboard-type style on-change placeholder placeholder-text-color]}]
  [react/text-input (merge {:placeholder placeholder}
                           (when placeholder-text-color {:placeholder-text-color placeholder-text-color})
                           (when style {:style style})
                           (when keyboard-type {:keyboard-type keyboard-type})
                           (when on-change
                             {:on-change-text #(re-frame/dispatch (on-change {:value %}))}))])

(defn touchable-opacity [{:keys [style on-press]} & children]
  (into [react/touchable-highlight (merge (when on-press {:on-press #(re-frame/dispatch (on-press {}))})
                                          (when style {:style style}))] children))

(defn image [{:keys [uri style]}]
  [react/image (merge {:style (merge {:width 100 :height 100} style)} {:source {:uri uri}})])

(defn link [{:keys [uri]}]
  [react/text
   {:style    {:color                colors/white
               :text-decoration-line :underline}
    :on-press #(re-frame/dispatch [:browser.ui/message-link-pressed uri])}
   uri])

(defn flat-list [{:keys [key data item-view]}]
  [react/scroll-view
   (for [item data]
     [item-view item])])
;[list/flat-list {:data data :key-fn (or key (fn [_ i] (str i))) :render-fn item-view}])

(defn checkbox [{:keys [on-change checked]}]
  [react/view {:style {:background-color colors/white}}
   [checkbox/checkbox {:checked?        checked
                       :style           {:padding 0}
                       :on-value-change #(re-frame/dispatch (on-change {:value %}))}]])

(defn activity-indicator-size [k]
  (condp = k
    :small "small"
    :large "large"
    nil))

(defn activity-indicator [{:keys [animating hides-when-stopped color size]}]
  [react/activity-indicator (merge (when animating {:animating animating})
                                   (when hides-when-stopped {:hidesWhenStopped hides-when-stopped})
                                   (when color {:color color})
                                   (when-let [size' (activity-indicator-size size)] {:size size'}))])

(defn text [o & children]
  (if (map? o)
    [react/text o children]
    (into [react/text {} o] children)))

(defn- wrap-view-child [child]
  (if (vector? child) child [text {} child]))

(defn abstract-view [type o & children]
  (if (map? o)
    (into [type o] (map wrap-view-child children))
    (into [type {} (wrap-view-child o)] (map wrap-view-child children))))

(defn view [o & children]
  (apply abstract-view react/view o children))

(defn scroll-view [o & children]
  (apply abstract-view react/scroll-view o children))

(defn icon [o]
  [icons/icon (:key o) o])

(defn component [])

(def capacities
  {:components {'view               {:value view}
                'text               {:value text}
                'scroll-view        {:value scroll-view :properties {:horizontal :boolean :keyboard-should-persist-taps :keyword :content-container-style :map}}
                'touchable-opacity  {:value touchable-opacity :properties {:on-press :event}}
                'icon               {:value icon :properties {:key :keyword :color :keyword}}
                'image              {:value image :properties {:uri :string}}
                'input              {:value input :properties {:on-change :event :placeholder :string :keyboard-type :keyword :placeholder-text-color :any}}
                'button             {:value button :properties {:enabled :boolean :disabled :boolean :on-click :event}}
                'link               {:value link :properties {:uri :string}}
                'list               {:value flat-list :properties {:data :vector :item-view :view :key? :keyword}}
                'checkbox           {:value checkbox :properties {:on-change :event :checked :boolean}}
                'activity-indicator {:value activity-indicator :properties {:animating :boolean :color :string :size :keyword :hides-when-stopped :boolean}}
                'picker             {:value component :properties {:on-change :event :selected :string :enabled :boolean :data :vector}}
                'nft-token-viewer   {:value component :properties {:token :string}}
                'transaction-status {:value component :properties {:outgoing :string :tx-hash :string}}}
   :queries    {'identity            {:value :extensions/identity :arguments {:value :map}}
                'store/get           {:value :store/get :arguments {:key :string}}
                'wallet/collectibles {:value :get-collectible-token :arguments {:token :string :symbol :string}}
                'wallet/balance      {:value :extensions.wallet/balance :arguments {:token :string}}
                'wallet/token        {:value :extensions.wallet/token :arguments {:token :string :amount? :numeric}}
                'wallet/tokens       {:value :extensions.wallet/tokens :arguments {:filter :vector}}}
   :events     {'identity
                {:permissions [:read]
                 :value       :extensions/identity-event
                 :arguments   {:cb :event}}
                'alert
                {:permissions [:read]
                 :value       :alert
                 :arguments   {:value :string}}
                'selection-screen
                {:permissions [:read]
                 :value       :extensions/show-selection-screen
                 :arguments   {:items :vector :on-select :event :render :view :title :string :extractor-key :keyword}}
                'chat.command/set-parameter
                {:permissions [:read]
                 :value       :extensions.chat.command/set-parameter
                 :arguments   {:value :any}}
                'chat.command/set-custom-parameter
                {:permissions [:read]
                 :value       :extensions.chat.command/set-custom-parameter
                 :arguments   {:key :keyword :value :any}}
                'chat.command/set-parameter-with-custom-params
                {:permissions [:read]
                 :value       :extensions.chat.command/set-parameter-with-custom-params
                 :arguments   {:value :string :params :map}}
                'chat.command/send-plain-text-message
                {:permissions [:read]
                 :value       :extensions.chat.command/send-plain-text-message
                 :arguments   {:value :string}}
                'chat.command/send-message
                {:permissions [:read]
                 :value       :extensions.chat.command/send-message
                 :arguments   {:params :map}}
                'log
                {:permissions [:read]
                 :value       :log
                 :arguments   {:value :string}}
                'arithmetic
                {:permissions [:read]
                 :value       :extensions/arithmetic
                 :arguments   {:values    #{:plus :minus :times :divide}
                               :operation :keyword
                               :on-result :event}}
                'schedule/start
                {:permissions [:read]
                 :value       :extensions/schedule-start
                 :arguments   {:interval   :number
                               :on-created :event
                               :on-result  :event}}
                'schedule/cancel
                {:permissions [:read]
                 :value       :extensions/schedule-cancel
                 :arguments   {:value      :number}}
                'json/parse
                {:permissions [:read]
                 :value       :extensions/json-parse
                 :arguments   {:value     :string
                               :on-result :event}}
                'json/stringify
                {:permissions [:read]
                 :value       :extensions/json-stringify
                 :arguments   {:value     :string
                               :on-result :event}}
                'store/put
                {:permissions [:read]
                 :value       :store/put
                 :arguments   {:key :string :value :any}}
                'store/puts
                {:permissions [:read]
                 :value       :store/puts
                 :arguments   {:value :vector}}
                'store/append
                {:permissions [:read]
                 :value       :store/append
                 :arguments   {:key :string :value :any}}
                'store/clear
                {:permissions [:read]
                 :value       :store/clear
                 :arguments   {:key :string}}
                'http/get
                {:permissions [:read]
                 :value       :http/get
                 :arguments   {:url         :string
                               :timeout?    :string
                               :on-success  :event
                               :on-failure? :event}}
                'http/post
                {:permissions [:read]
                 :value       :http/post
                 :arguments   {:url         :string
                               :body        :string
                               :timeout?    :string
                               :on-success  :event
                               :on-failure? :event}}
                'ipfs/cat
                {:permissions [:read]
                 :value       :ipfs/cat
                 :arguments   {:hash        :string
                               :on-success  :event
                               :on-failure? :event}}
                'ethereum/transaction-receipt
                {:permissions [:read]
                 :value       :extensions/ethereum-transaction-receipt
                 :arguments   {:value     :string
                               :on-result :event}}
                'ethereum/sign
                {:permissions [:read]
                 :value       :extensions/ethereum-sign
                 :arguments   {:message?   :string
                               :data?      :string
                               :on-result  :event}}
                'ethereum/send-transaction
                {:permissions [:read]
                 :value       :extensions/ethereum-send-transaction
                 :arguments   {:to         :string
                               :gas?       :string
                               :gas-price? :string
                               :value?     :string
                               :method?    :string
                               :params?    :vector
                               :nonce?     :string
                               :on-result  :event}}
                'ethereum/logs
                {:permissions [:read]
                 :value       :extensions/ethereum-logs
                 :arguments   {:fromBlock? :string
                               :toBlock?   :string
                               :address?   :vector
                               :topics?    :vector
                               :blockhash? :string
                               :on-result  :event}}
                'ethereum/resolve-ens
                {:permissions [:read]
                 :value       :extensions/ethereum-resolve-ens
                 :arguments   {:name      :string
                               :on-result :event}}
                'ethereum/call
                {:permissions [:read]
                 :value       :extensions/ethereum-call
                 :arguments   {:to        :string
                               :method    :string
                               :params?   :vector
                               :outputs?  :vector
                               :on-result :event}}}
   :hooks      {:wallet.settings hooks/settings-hook
                :chat.command    hooks/command-hook}})

(defn read [code-string]
  (reader/read code-string))

(defn parse [data]
  (reader/parse {:capacities capacities} data))