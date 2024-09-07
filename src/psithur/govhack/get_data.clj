(ns psithur.govhack.get-data
  (:require
    [charred.api :as charred]
    [clj-http.client :as http]
    [clojure.string]
    [hickory.core :as h]
    [hickory.select :as s]
    [hickory.zip :as hzip]
    [tick.core :as t])
  (:import [java.net URLEncoder]))

(def parse-json (charred/parse-json-fn))
(defn escape-uri
  [s]
  (URLEncoder/encode s "UTF-8"))

(defn convert-url
  [url]
  (clojure.string/replace
    url
    #"http[s]?://www\.aph\.gov\.au/Parliamentary_Business/Hansard/Hansard_Display\?bid=([^&]+)&sid=(.*)"
    "$1$2"))

(defn list-hansards
  []
  (let [page (-> "https://www.aph.gov.au/Parliamentary_Business/Hansard/Hansreps_2011"
                 (http/get)
                 :body
                 (h/parse)
                 (h/as-hickory))]

    (->> (s/select (s/tag :a) page)
         (keep (fn [{:keys [attrs]}]
                 (when-let [href (:href attrs)]
                   (when (clojure.string/starts-with? (last (clojure.string/split href #"//"))
                                                      "www.aph.gov.au/Parliamentary_Business/Hansard/Hansard_Display")
                     href))))
         (map convert-url))))

(def base-url "https://www.aph.gov.au/api/hansard/transcript?id=")

(defn get-hansard
  [hansard-tok]
  (let [uri     (str base-url (escape-uri hansard-tok))
        page    (-> uri
                    (http/get)
                    :body
                    parse-json)
        xml-uri (get page "ViewSaveXMLLink")]
    (prn "Getting" xml-uri)
    (-> xml-uri
        (http/get
          {:headers
           ; we get a 403 without this UA...
           {"User-Agent"
            "Mozilla/5.0 (X11); Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"}})
        :body)))

(defn file-path-exists?
  [path]
  (let [file (java.io.File. (str "resources/" path))]
    (if (.exists file)
      true
      (do (.mkdirs (.getParentFile file)) ; Create parent directories if needed
          file)))) ; Create the file

(defn get-all-hansards!
  []
  (let [hansards (list-hansards)]
    (doseq [hansard hansards]
      (let [fp (file-path-exists? hansard)]
        (if (true? fp) (prn "Already have" hansard) (spit fp (get-hansard hansard)))))))



(def hidden
  [
   "chamber/hansardr/d30367d6-fcb1-45a9-a355-48501df65ab8/0000"
   "chamber/hansardr/6c17fac8-87dd-48c2-9a3e-2df8dcfe9dbc/0000"
   "chamber/hansardr/233d70ab-f831-4546-b2cb-6efab40ce3d1/0000"
   "chamber/hansardr/cb768683-f6bc-44e8-8348-c6bd1dba5e12/0000"
   "chamber/hansardr/42b54043-1776-4bc5-87c1-aeb0955beccd/0000"
   "chamber/hansardr/8b10b3bf-8742-4d78-816b-229c36cb58f0/0000"
   "chamber/hansardr/ed537be9-743e-4c82-9710-d16b59d9ad54/0000"
   "chamber/hansardr/6ab7607c-a6db-41b2-96b3-1b2a6f2fb729/0000"
   "chamber/hansardr/dc5babf6-d13a-4d24-9679-f44e8a7ac864/0000"
   "chamber/hansardr/aeba9c2b-00c4-4d71-a914-573a3542d67c/0000"
   "chamber/hansardr/02641561-83f9-44d9-905e-7ca9e91f89c8/0000"
   "chamber/hansardr/65316ec4-a2b9-4874-99f3-828be1b9a7ab/0000"
   "chamber/hansardr/f64ad34c-669f-4921-8ec4-7e5a6fee2dc9/0000"
   "chamber/hansardr/4394ccf3-ce8f-42d0-ab37-f4f10a14ce3a/0000"
   "chamber/hansardr/91e391d8-9abc-4aa0-abd2-55dcc9b07328/0000"
   "chamber/hansardr/311d5444-ac02-4c7a-aca5-e3d5a5e1c2a7/0000"
   "chamber/hansardr/42ae01ac-b8f5-4979-9e32-17646d2f8757/0000"
   "chamber/hansardr/58e670db-9166-4e71-9a76-2f7ac1872d41/0000"
   "chamber/hansardr/f16c26f7-7d81-47f6-b117-32cd0df0510e/0000"
   "chamber/hansardr/2b8f8c5f-a053-4e0c-bdb8-521bbdd9daa9/0000"
   "chamber/hansardr/c88bcc8d-faad-4e95-bc78-521f9b65c7d2/0000"
   "chamber/hansardr/a778b382-c7e2-48b6-8516-4e283ae68381/0000"
   "chamber/hansardr/3aa7b06b-a011-4392-8a1a-80ecb19d5f1a/0000"
   "chamber/hansardr/fbcfdc92-11b6-4cc9-a34b-5ee91153745b/0000"
   "chamber/hansardr/5005d996-a229-4d88-a18a-97e4c70215a3/0000"
   "chamber/hansardr/2f554bb1-776c-4c6f-b693-214bf49ecaa3/0000"
   "chamber/hansardr/832d97e7-5731-4e03-b7e4-02ee76be9d56/0000"
   "chamber/hansardr/d3993cc9-3d63-4cfa-8fd2-f8ea3e04b02a/0000"
   "chamber/hansardr/f96d2bdc-a977-427b-96fd-7f167111d0a6/0000"
   "chamber/hansardr/f50fa418-a190-422a-a1cf-caf814fb6b3b/0000"
   "chamber/hansardr/494c836a-e9b4-4f50-9c2b-57185780ab51/0000"
   "chamber/hansardr/22e09eab-375e-4fdf-8c4f-b2f95c1adda9/0000"
   "chamber/hansardr/3a9e2f62-c423-4ddc-bd22-1c2da89c6d5b/0000"
   "chamber/hansardr/d6700ba7-bfbc-40cf-a2aa-1e339ff1e843/0000"
   "chamber/hansardr/08bc85b3-4864-41db-a1a9-fb4b1059cda0/0000"
   "chamber/hansardr/554a0b22-6b34-4680-9d45-c5d3dc4ab147/0000"
   "chamber/hansardr/a33a2fb9-7912-4b27-a866-1ef677c9c0cc/0000"
   "chamber/hansardr/87b64401-ccd0-4a54-a4df-cef0e9eed88f/0000"
   "chamber/hansardr/bcc6cf79-e37b-4f70-9a20-0ddc17522ca5/0000"
   "chamber/hansardr/f6a32527-fe10-4a4c-8285-050db6525c2a/0000"
   "chamber/hansardr/70f7cd4b-ef56-4cd9-8b18-efa80a95fcaa/0000"
   "chamber/hansardr/a42cf478-4ae3-41a7-a650-a236ec98ee4c/0000"
   "chamber/hansardr/d8e9f7e7-79fc-4b4c-8db2-5934a5e59490/0000"
   "chamber/hansardr/1fd076c1-3286-4f4f-97a1-dd0e93035019/0000"
   "chamber/hansardr/6dc8dc94-b7fc-45eb-a84f-1f959010bf23/0000"
   "chamber/hansardr/fd2f3451-f05d-425a-9815-471294607839/0000"
   "chamber/hansardr/c737e440-6de7-4ab7-a383-e9cc80648ecf/0000"
   "chamber/hansardr/312abf5f-a8c0-41ad-bae7-d5f95017e3dd/0000"
   "chamber/hansardr/49f7fdf7-bb7d-492a-b6cb-7f7f527fbeaa/0000"
   "chamber/hansardr/cd6813da-3cd0-4342-ab78-5445603e1c18/0000"
   "chamber/hansardr/3a654fac-ced2-4c1d-90c3-e3ad3511096e/0000"
   "chamber/hansardr/addc42b0-ec46-4a00-8fa0-7bca0bab2e06/0000"
   "chamber/hansardr/9b1d0612-2568-4a39-bfb6-9dc4c98c1222/0000"
   "chamber/hansardr/842ee7d9-89d4-4e4f-bc93-045b018bbeb2/0000"
   "chamber/hansardr/108deaf7-f29c-4e1f-96aa-3f9b4ff9bf1a/0000"
   "chamber/hansardr/1d38c79d-f3f8-401a-81d0-e364715774a5/0000"
   "chamber/hansardr/b1239e0c-7fca-43e4-aac5-25ac64e2bc96/0000"
   "chamber/hansardr/9719682d-09af-4772-a03a-a954ad5d7b6a/0000"
   "chamber/hansardr/3aef5f9d-aa5b-4d1d-98d0-fa5f25d3d3d7/0000"
   "chamber/hansardr/404ed0f8-1c19-423e-9127-242048dd2482/0000"
   "chamber/hansardr/51ef71ed-8735-4265-917d-e3ae650d4de6/0000"
   "chamber/hansardr/b15942d6-e86a-4a01-8094-d46337096349/0000"
   "chamber/hansardr/93a1fa0d-0b2c-453c-bbeb-b700dd4555c5/0000"
   "chamber/hansardr/83fc8f93-4bbf-4d67-982c-cec218025c2f/0000"
   "chamber/hansardr/d99ea81a-fe39-407e-a97e-1dc6277a1036/0000"
   "chamber/hansardr/b832e8e0-4110-4988-b4fe-f0aa6fccf5cf/0000"
   "chamber/hansardr/e1a2d5a5-26ab-4d1a-b41c-6f8d732b1ba8/0000"
   "chamber/hansardr/4542f24a-411d-41aa-874c-00a76d8af37f/0000"
   "chamber/hansardr/144a4e74-4756-456a-8c15-e2a29f2b296b/0000"
   "chamber/hansardr/02ea44d9-3064-475f-97e4-cc2d68b630b6/0000"
   "chamber/hansardr/83163a85-c413-4318-b2e3-8cd22553fd24/0000"
   "chamber/hansardr/59b04f0a-5493-4673-9b3d-00db9f8bd3fe/0000"
   "chamber/hansardr/d6a2fc72-bd7c-407a-ae8f-a96400c5d69f/0000"
   "chamber/hansardr/19a0a2a6-673e-4aee-8a78-d39a4d56068c/0000"
   "chamber/hansardr/9739759a-c55b-417a-aa76-1072aaf7e717/0000"
   "chamber/hansardr/55d46158-f865-4a9f-9015-36543a3b6b7b/0000"
   "chamber/hansardr/d5f2441d-bbaa-47cd-9b30-b2e54878e868/0000"
   "chamber/hansardr/58a73839-3036-43f5-967d-8540f8c9a3db/0000"
   "chamber/hansardr/bb3ef167-e84b-40fc-bff9-814fb0e492d2/0000"
   "chamber/hansardr/8ab90ee4-4daf-4562-81de-62f4ff657b8d/0000"
   "chamber/hansardr/3c6f5db6-8f8b-4848-b3cb-aba0b158565b/0000"
   "chamber/hansardr/43a669e8-9d2f-411b-bacb-ffc094fd0f4a/0000"
   "chamber/hansardr/e4d9989e-eae9-4f90-94c0-daae2dc39ec3/0000"
   "chamber/hansardr/164c9073-1804-484e-b7a0-2abda35eb487/0000"
   "chamber/hansardr/a7b8c611-e859-4bc8-90a6-76de1001a02c/0000"
   "chamber/hansardr/a9f05bc4-7c90-43df-aef9-8700be3bdda7/0000"
   "chamber/hansardr/19c32232-832c-45aa-8efa-783c9a3446a4/0000"
   "chamber/hansardr/349fbf1e-3729-43d8-af42-e282d6de779b/0000"
   "chamber/hansardr/fc34435e-5f7a-4507-bac8-a38614e5fd1e/0000"
   "chamber/hansardr/82b1971c-010c-45a2-b5cf-5d6fc99e9724/0000"
   "chamber/hansardr/8a8460f3-1972-403b-acbb-d8679c9f1fc3/0000"
   "chamber/hansardr/eca281a0-1afd-478f-b8d8-8e95c9aac2cf/0000"
   "chamber/hansardr/d85512b4-8807-4aff-947c-c3b2e4df6446/0000"
   "chamber/hansardr/ad6e1917-440e-4f90-acad-16d2af135e93/0000"
   "chamber/hansardr/6927fabc-0527-4e69-a201-0d62ea4f32f8/0000"
   "chamber/hansardr/40c69b08-1375-4362-8f90-6bf646a9cada/0000"
   "chamber/hansardr/b93d9c6e-c89b-4e3b-815b-42d1e54f2e99/0000"
   "chamber/hansardr/6f011bde-9138-44d1-93a9-08f7c4113f58/0000"
   "chamber/hansardr/52343173-f805-4dee-a657-0b931f78bc41/0000"
   "chamber/hansardr/2e31003b-2dbb-4266-99df-7a1e64facc8c/0000"
   "chamber/hansardr/ad022329-595b-422a-b4d6-0600ddff0c7b/0000"
   "chamber/hansardr/a65ad565-3e46-4334-aebc-4d070cfdbbc4/0000"
   "chamber/hansardr/404412bc-0281-4b72-9ee4-b01c32558b05/0000"
   "chamber/hansardr/af682406-aa83-4bc9-9c73-65d22c87cca9/0000"
   "chamber/hansardr/1b09bdff-83b9-4530-a94e-a7856b753e0b/0000"
   "chamber/hansardr/128494f5-3b78-4d37-9efd-c8ccbc721e70/0000"
   "chamber/hansardr/89bbc84e-d632-4f44-974d-194bc08b5a26/0000"
   "chamber/hansardr/b7c3903c-728e-40bc-91f4-728ffe55978d/0000"
   "chamber/hansardr/699540ac-d990-4325-b983-f5353c43533b/0000"
   "chamber/hansardr/83dad351-037c-4a7e-a123-81b1d0c8126d/0000"
   "chamber/hansardr/bde428ce-609b-4a97-86eb-475cba8fec85/0000"
   "chamber/hansardr/0b40817f-a75a-4ce9-b4bd-0a2dcd932168/0000"
   "chamber/hansardr/db5f6838-aac3-4986-a738-656c5facebf6/0000"
   "chamber/hansardr/3060e0ca-b9e8-4414-bc3b-1c7e6d66cbd6/0000"
   "chamber/hansardr/1ab79a37-6469-4f02-ac54-0cbcf32f5976/0000"
   "chamber/hansardr/16ed2385-25fe-4c7f-93d8-8ac7bcdb45e8/0000"
   "chamber/hansardr/2b9132d9-d5d3-4d73-a53a-ac3597d3bdb5/0000"
   "chamber/hansardr/9c18a223-4397-465e-bb44-fdddfbae3cf2/0000"
   "chamber/hansardr/3d1f8712-ae58-466e-97e3-7d91fe79d4e8/0000"
   "chamber/hansardr/5b8a27a1-b10c-4d76-9923-098c7b4fb463/0000"
   "chamber/hansardr/ba833987-787f-453f-b339-92c909ac1878/0000"
   "chamber/hansardr/8fd8920a-0076-4086-896a-7402223f6f5d/0000"
   "chamber/hansardr/dae248ad-93bb-4fb3-8ba5-e5b566b992ad/0000"
   "chamber/hansardr/42f3d9a6-12f4-4844-a619-b4e04362b17b/0000"
   "chamber/hansardr/512317ee-6094-4e3d-88f0-38ac38d28920/0000"
   "chamber/hansardr/cf028b00-427a-488c-8c92-2792de44c147/0000"
   "chamber/hansardr/3844e8bb-e9a0-4993-83b8-a9bd8f2b17d7/0000"
   "chamber/hansardr/7a89c07c-522e-4e24-9643-d3209145b417/0000"
   "chamber/hansardr/a526371b-b2dd-4037-ba7a-649c0c3fb696/0000"
   "chamber/hansardr/424ea1a9-5ac4-4b91-b18e-c93201d11e75/0000"
   "chamber/hansardr/88794398-a5c7-4145-9d35-ec4d87b33631/0000"
   "chamber/hansardr/2f675762-2571-4268-b435-28ce82b5e26c/0000"
   "chamber/hansardr/616d83e8-a028-46e9-a6c3-35b73a09ffc0/0000"
   "chamber/hansardr/22c95bd6-3bc8-4279-b869-fc511af2ce7d/0000"
   "chamber/hansardr/a275472e-b699-46e7-ac29-bcf2fb8ee942/0000"
   "chamber/hansardr/4916447f-6ab8-4251-9d7e-93017c2ba328/0000"
   "chamber/hansardr/db57a706-8c03-4a70-83a2-831cc00cf088/0000"
   "chamber/hansardr/a551d60b-c354-4ceb-885e-4c9a62244838/0000"
   "chamber/hansardr/8aae74c9-d651-4c2e-b817-cc2575bcf29a/0000"
   "chamber/hansardr/4a3ea2e7-05f5-4423-88aa-f33e93256485/0000"
   "chamber/hansardr/156b7ec9-1215-4e5d-b6a4-ccfb86ead68b/0000"
   "chamber/hansardr/35c9c2cf-9347-4a82-be89-20df5f76529b/0000"
   "chamber/hansardr/8f9d6b15-68fd-4bdc-8c17-9f8bdc5de7af/0000"
   "chamber/hansardr/3035fcd2-721b-41b5-b5be-42bc6b1b9f8e/0000"
   "chamber/hansardr/b15d3630-9dab-460a-8a09-a47777da07c2/0000"
   "chamber/hansardr/34c85a11-8679-4b42-9fe4-ae9085251b8e/0000"
   "chamber/hansardr/07c1718f-8e51-4958-9cc9-f8492bfb5c93/0000"
   "chamber/hansardr/2d891fab-c2b5-41b4-967f-0b37fdb6fe7c/0000"
   "chamber/hansardr/205b6a7a-8254-419a-9092-75b6e181902d/0000"
   "chamber/hansardr/b1d2e0ad-97f9-41e8-b5ac-42a061730951/0000"
   "chamber/hansardr/200bef5f-bafa-4738-b150-7dbac05c37e0/0000"
   "chamber/hansardr/2ed4cf5b-58cd-4718-a97b-be0a93bc99b4/0000"
   "chamber/hansardr/27c8c7aa-70e7-442b-8ceb-93bd0691de92/0000"
   "chamber/hansardr/624d0842-94f0-49a5-b301-f2d86f83d947/0000"
   "chamber/hansardr/5e3b7f89-dcdf-4e27-919a-1183ececfed8/0000"
   "chamber/hansardr/e1b9741b-6117-42e6-bb54-219d93714fe7/0000"
   "chamber/hansardr/ff317f2a-87e3-4fa6-ad9e-b2a9d7263fb3/0000"
   "chamber/hansardr/99d0f7c3-3aa4-4fae-bdd1-77a0cbf2e5c9/0000"
   "chamber/hansardr/004ccb33-581e-44b3-8c5c-bbb56bbc99f3/0000"
   "chamber/hansardr/ff950324-5094-4dbd-bbd3-4de23350730c/0000"
   "chamber/hansardr/9ab2c98f-3835-4f5f-a7b5-d382a0f0d4fc/0000"
   "chamber/hansardr/2ad63848-b3cb-42ba-9d7c-0aaf29f36d20/0000"
   "chamber/hansardr/8143f75e-7f37-4128-8d3b-e62455d99a32/0000"
   "chamber/hansardr/7d2bdc3b-35ed-4264-bd05-2f4e82a3829f/0000"
   "chamber/hansardr/9876e2b9-5961-455f-aa38-455af1c59cb8/0000"
   "chamber/hansardr/be0df22c-0b6f-4a7b-a517-08bec7742457/0000"
   "chamber/hansardr/135b167f-578d-4414-a5b0-b2a35bc1cc32/0000"
   "chamber/hansardr/69dfc543-83f2-4d01-b4e5-b3ac903c117e/0000"
   "chamber/hansardr/6c45e63e-5ef2-42f0-aaec-817d6436edec/0000"
   "chamber/hansardr/9887dbf0-2eba-448e-82db-0288b44668a2/0000"
   "chamber/hansardr/30431c55-8b12-46e8-9c61-f8cee4edee63/0000"
   "chamber/hansardr/9526da6b-9674-4509-a6d5-a7115a7c1f1a/0000"
   "chamber/hansardr/e40cc95f-8da2-4485-8b5c-f83df5fee682/0000"
   "chamber/hansardr/4d8018b9-21a1-4483-815f-6755083d793b/0000"
   "chamber/hansardr/2e1f246a-60ce-487a-a9a5-0ecd042901f7/0000"
   "chamber/hansardr/5b6d8876-4831-481f-8b5b-85bb0fbe651f/0000"
   "chamber/hansardr/c88fc48d-9cc9-4423-9cc5-70899d3b4627/0000"
   "chamber/hansardr/63afe481-6097-4111-97a1-ade20535e053/0000"
   "chamber/hansardr/dd8fab1f-c4e4-4b85-bf93-8f1a9c7cba8e/0000"
   "chamber/hansardr/2eef31f5-7b33-4563-b8f6-7aaab944f4d2/0000"
   "chamber/hansardr/2664e920-4f1e-4c71-99c4-5a88ac43e482/0000"
   "chamber/hansardr/4d60a662-a538-4e48-b2d8-9a97b8276c77/0000"
   "chamber/hansardr/d4d17c1a-fc1c-44c7-9f9c-811a074b90ec/0000"
   "chamber/hansardr/bd34d03c-cbbe-4b65-a047-0a02e9793b9b/0000"
   "chamber/hansardr/d93bc084-2c64-4d16-99f7-8c6872537c13/0000"
   "chamber/hansardr/aec24641-694d-4aba-adb3-c0e00179d7a4/0000"
   "chamber/hansardr/585f8e84-c281-4991-9ab8-437f9b9ff0f8/0000"
   "chamber/hansardr/cbaa06fe-b02e-4ba8-9fde-fd279b7b8579/0000"
   "chamber/hansardr/e674bc2a-82df-4a25-981b-1f2bab3d0b16/0000"
   "chamber/hansardr/0f5efc81-3e5b-496c-81dd-93d95de14659/0000"
   "chamber/hansardr/7c9ab1c0-b8d7-4984-afcf-e66510c0be9b/0000"
   "chamber/hansardr/af7f2b6d-1ae3-4be8-ac66-e69bab8105a8/0000"
   "chamber/hansardr/70599486-f0f8-459f-87b4-af23cfca767f/0000"
   "chamber/hansardr/b408ffb8-34a8-4da1-bb08-aaa9dfc02b0e/0000"
   "chamber/hansardr/5f0f5c44-95ba-4857-8d00-f969c7b99627/0000"
   "chamber/hansardr/5c41e365-7ca7-47aa-a810-1c6e44b75e5e/0000"
   "chamber/hansardr/72de5436-cc84-439a-b82f-89066b3333e9/0000"
   "chamber/hansardr/842f76c4-4334-4196-a526-122187fa614f/0000"
   "chamber/hansardr/abbbcf3b-f1cf-40d7-bf3e-751a75751885/0000"
   "chamber/hansardr/6c0c6a1a-65b5-4c98-a354-9aec64f38f5f/0000"
   "chamber/hansardr/3204e1b6-d6f4-4b90-9d70-f9a8b0936429/0000"
   "chamber/hansardr/26088656-df77-4f73-8605-47271c5ff5cf/0000"
   "chamber/hansardr/a65a0414-5b1f-4110-8b65-7577b8aa9d55/0000"
   "chamber/hansardr/60564cf1-8e98-4f07-8d0e-51f3e8dd47ca/0000"
   "chamber/hansardr/b3c62e6f-56d4-43a4-be41-fa828e5ffa1f/0000"
   "chamber/hansardr/dfa1725a-aa28-48db-ba56-7995bad31f8c/0000"
   "chamber/hansardr/c34bef48-297b-4a1e-9e19-cb3482680115/0000"
   "chamber/hansardr/89274c8f-2468-4c73-b7cf-69715d12aa15/0000"
   "chamber/hansardr/bda27a36-a8b5-4e6a-a64f-6084b2c53511/0000"
   "chamber/hansardr/6f383439-23d7-4827-97ae-696b940b452c/0000"
   "chamber/hansardr/4ebaa8b7-b909-4262-932a-960ef76e20ad/0000"
   "chamber/hansardr/fd07d20c-612a-4cfc-9907-eccf3865dec5/0000"
   "chamber/hansardr/451460c0-4232-4947-a01e-30cf827a8e30/0000"
   "chamber/hansardr/681b54e7-f58c-4177-a0e4-d7f2473f2120/0000"
   "chamber/hansardr/cd08e91c-1ffc-4955-a61a-1a9087358780/0000"
   "chamber/hansardr/843adba4-b07b-4642-9c44-98beb898a1b5/0000"
   "chamber/hansardr/1c0dbef0-05d4-42fa-89c8-ae7e3958767b/0000"
   "chamber/hansardr/fdab017b-97a3-4480-a3f7-e214d5c068b2/0000"
   "chamber/hansardr/2fb0005f-4976-4319-8a09-f98ad79b5a64/0000"
   "chamber/hansardr/abc2c0c8-7187-4566-b4d3-a8f9194f0813/0000"
   "chamber/hansardr/e2ddd128-129a-4173-bcb6-19e542b34555/0000"
   "chamber/hansardr/a097ab46-bef0-4ed3-b3f0-27f3b075e04e/0000"
   "chamber/hansardr/7b0b2bac-de69-42c1-8a98-2d16329f051f/0000"
   "chamber/hansardr/2a23625b-4f80-4806-bdea-cf22f74e9fac/0000"
   "chamber/hansardr/4011f121-4ade-4f4b-89bf-3918b5ee0697/0000"
   "chamber/hansardr/4a17e30d-c43b-48b9-83ed-4280fc00314c/0000"
   "chamber/hansardr/10837263-363d-4f9e-9020-2bdf266fb589/0000"
   "chamber/hansardr/8f9c3c2a-1ee7-4657-a602-aef97dd32610/0000"
   "chamber/hansardr/f5daa851-cc46-4e6e-915e-00fecec9e052/0000"
   "chamber/hansardr/9f5924ba-395f-4c40-95c7-74c91ef8cd12/0000"
   "chamber/hansardr/c3691419-911e-4e11-b6bf-09c7da360bc2/0000"
   "chamber/hansardr/7503935a-ef9f-47a5-9ab8-7a228d52f809/0000"
   "chamber/hansardr/b9c79f46-7f3a-4739-acb9-ec8d676c1b6b/0000"
   "chamber/hansardr/3e4e9532-bf3c-4623-bc6b-c0e926ad7cec/0000"
   "chamber/hansardr/38b26896-2425-4184-83c1-0584f5b1cf86/0000"
   "chamber/hansardr/b3153d12-39f7-4135-bb56-f699ce3218b6/0000"
   "chamber/hansardr/e62f1e0d-13c9-446f-9296-82fd78823b75/0000"
   "chamber/hansardr/a8785071-a01e-4d0b-bf4e-9e6ea9104940/0000"
   "chamber/hansardr/d75f3d9f-3eb9-4f52-b197-0b7e87567daa/0000"
   "chamber/hansardr/2d5112d9-d29f-4121-aa4c-332814189972/0000"
   "chamber/hansardr/34fd7cc2-1683-44b7-ae67-c8a75a3cbdca/0000"
   "chamber/hansardr/128166ac-3803-48a6-ba49-c2e6241fb5ed/0000"
   "chamber/hansardr/0a139982-0035-4594-acfa-06b59eae3613/0000"
   "chamber/hansardr/e8ea63c4-120c-43d4-a7e2-8df31d741111/0000"
   "chamber/hansardr/de045419-4cf3-4a48-a502-ec68c5e81782/0000"
   "chamber/hansardr/4330bb6a-8956-441b-b34b-bb67cfec9fac/0000"
   "chamber/hansardr/349cd40e-55d4-4907-8ae0-0b25b483b9b2/0000"
   "chamber/hansardr/abd3301a-19e7-46b0-b8af-d07af4a594f0/0000"
   "chamber/hansardr/136b90b1-cf84-467a-9150-fe3c3b5cacda/0000"
   "chamber/hansardr/a0c9c3b1-a08d-42ca-995d-7f4622b624e7/0000"
   "chamber/hansardr/5a0ebb6b-c6c8-4a92-ac13-219423c2048d/0000"
   "chamber/hansardr/64910c49-3706-419c-93d6-134834c0ae37/0000"
   "chamber/hansardr/326c1d2a-eacb-4d75-8dfe-810eeaf66e7c/0000"
   "chamber/hansardr/15cdb75b-d437-4738-8c1e-7d076927a391/0000"
   "chamber/hansardr/f3460e8f-bde4-4fc5-8d67-52fdc5190016/0000"
   "chamber/hansardr/466695ee-0c7f-4e03-8a6d-246f5f265306/0000"
   "chamber/hansardr/a99811dc-5385-4eff-88af-aa8ad29fe367/0000"
   "chamber/hansardr/c94905e2-4370-462f-b408-d06e0d0a5c8e/0000"
   "chamber/hansardr/2a280591-bf99-4a04-9a9b-1b4c74ca6d9b/0000"
   "chamber/hansardr/50ef4858-02bd-437b-a64f-599769ecfec6/0000"
   "chamber/hansardr/9b96ae59-96ca-4e39-b984-8b520b432ef5/0000"])

(defn get-other-hansards!
  "these weren't picked up originally because the page uses javascript to hide older transcripts"
  []
  (let [hansards hidden]
    (doseq [hansard hansards]
      (let [fp (file-path-exists? hansard)]
        (if (true? fp) (prn "Already have" hansard) (spit fp (get-hansard hansard)))))))
