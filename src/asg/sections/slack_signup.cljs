(ns asg.sections.slack-signup
  (:require [reagent.core :as r]
            [asg.styles.alignment :as align]
            ["react-google-recaptcha" :default recaptcha]
            [asg.styles.typography :as typo]
            [clojure.string :as str]))

(def client-key "6Lfx6ZcUAAAAANfkT0wc8BYKRVfXdr8vEk7fhe6w")

(def email-regex (re-pattern "^[-!#$%&'*+\\/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+\\/0-9=?A-Z^_a-z`{|}~])*@[a-zA-Z0-9](-*\\.?[a-zA-Z0-9])*\\.[a-zA-Z](-?[a-zA-Z0-9])+$"))

(def valid-email?
  (every-pred identity
              #(< (count %) 254)
              #(re-matches email-regex %)
              #(< 0 (str/index-of % "@") 64)
              #(let [parts         (str/split % ".")
                     short-enough? (fn [s] (< (count s) 63))]
                 (every? short-enough? parts))))

(defn input [state new-state]
  [:input {:style     {:border-color (if (or (str/blank? (:email @state))
                                             (:submittable? @state))
                                       "grey"
                                       "red")}
           :value     (:email @state)
           :type      "text"
           :on-change #(new-state :email (.. % -target -value))}])

(defn submit [state]
  [:button {:disabled (not (:submittable? @state))
            :type     "submit"}
   "Submit"])

(def signup-url "https://wnephqc0h5.execute-api.us-east-1.amazonaws.com/prod/slack")

(def state  (r/atom {:submittable? false
                     :stage        :editing
                     :email        ""
                     :captcha-token nil}))

(defn mark-valid-state [{:keys [email captcha-token] :as state}]
  (assoc state :submittable? (and (valid-email? email)
                                  (some? captcha-token))))

(defn state->send-payload [{:keys [email captcha-token]}]
  #js {:email email :recaptchaToken captcha-token})

(defn Signup []
  (let [new-state (fn [k v]
                    (swap! state #(mark-valid-state (assoc % k v))))
        send! (fn [payload]
                 (swap! state assoc :stage :sending)
                 ;; fetch says this request is not OK. Not sure why
                 (-> (js/fetch signup-url
                               #js {:method "POST"
                                    :mode "no-cors"
                                    :cache "no-cache"
                                    :credentials "same-origin"
                                    ;; todo: not sure these headers
                                    ;; are set correctly
                                    ;; https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
                                    :headers #js {"Content-Type" "application/json"}
                                    :body (.stringify js/JSON payload)})
                     (.then (fn [response]
                              (swap! state assoc :stage :sent :captcha-token nil)))
                     (.catch (fn [response]
                               (swap! state assoc :stage :error)))))]
    [align/Container
     [typo/section-header {:text "Slack Signup"
                           :id   "join"}]
     (case (:stage @state)
       :editing
       [:form {:on-submit #(do (.preventDefault %)
                               (when (:submittable? @state)
                                 (send! (state->send-payload @state))))}
        [:> recaptcha
         {:sitekey client-key
          :onChange (fn [token] (new-state :captcha-token token))
          :style {:margin "2em"
                  :display "flex"
                  :justify-content "center"}}]
        [input state new-state]
        [submit state]]
       :sending
       [:h1 "sending state"]
       :sent
       [:h1 "accepted state"]
       :error
       [:h1 "error state"])]))
