# GWH_TM_CUST
#Entity #Standard #MASTER

| COLUMN_NAME        | DATA_TYPE           | NULLABLE                           | DATA_DEFAULT | COLUMN_ID | COMMENTS                                                                         |
|--------------------|---------------------|------------------------------------|--------------|-----------|----------------------------------------------------------------------------------|
| DEL_FLG            | CHAR(1 BYTE)        | No                                 |              | 1         | Delete Flag / 論理削除フラグ                                                            |
| CRT_YMD            | CHAR(8 BYTE)        | No                                 |              | 2         | Create Date (UTC) / 作成日(標準時)                                                     |
| CRT_TIM            | CHAR(6 BYTE)        | No                                 |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                                                    |
| CRT_TMID           | NVARCHAR2(100 CHAR) | No                                 |              | 4         | Create Term Id / 作成端末ID                                                          |
| CRT_USER           | NVARCHAR2(50 CHAR)  | No                                 |              | 5         | Create User Id / 作成ユーザID                                                         |
| CRT_PGM            | NVARCHAR2(30 CHAR)  | No                                 |              | 6         | Create Program Id / 作成プログラムID                                                    |
| CRT_TM_ZONE        | NVARCHAR2(3 CHAR)   | No                                 |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                                              |
| CRT_YMDHMS         | TIMESTAMP(6)        | No                                 |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）                                         |
| CRT_L_YMDHMS       | TIMESTAMP(6)        | No                                 |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）                                      |
| UPD_YMD            | CHAR(8 BYTE)        | No                                 |              | 10        | Update Date (UTC) / 更新日(標準時)                                                     |
| UPD_TIM            | CHAR(6 BYTE)        | No                                 |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                                                    |
| UPD_TMID           | NVARCHAR2(100 CHAR) | No                                 |              | 12        | Update Terminal ID / 更新端末ID                                                      |
| UPD_USER           | NVARCHAR2(50 CHAR)  | No                                 |              | 13        | Update User ID / 更新ユーザID                                                         |
| UPD_PGM            | NVARCHAR2(30 CHAR)  | No                                 |              | 14        | Update Program ID / 更新プログラムID                                                    |
| UPD_TM_ZONE        | NVARCHAR2(3 CHAR)   | No                                 |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                                              |
| UPD_YMDHMS         | TIMESTAMP(6)        | No                                 |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）                                         |
| UPD_L_YMDHMS       | TIMESTAMP(6)        | No                                 |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）                                      |
| CUST_CPNY_COD      | NVARCHAR2(6 CHAR)   | No                                 |              | 18        | Company Code / カンパニーコード                                                          |
| CUST_WHS_COD       | NVARCHAR2(4 CHAR)   | No                                 |              | 19        | Warehouse Code / 倉庫コード                                                           |
| CUST_COD           | NVARCHAR2(13 CHAR)  | No                                 |              | 20        | Customer Code / 顧客コード                                                            |
| CUST_NAM1          | NVARCHAR2(50 CHAR)  | Yes                                |              | 21        | Customer Name 1St / 顧客名称1st                                                      |
| CUST_NAM2          | NVARCHAR2(50 CHAR)  | Yes                                |              | 22        | Customer Name 2Nd / 顧客名称2nd                                                      |
| CUST_ZIP           | NVARCHAR2(10 CHAR)  | Yes                                |              | 23        | Customer Zip / 顧客郵便番号                                                            |
| CUST_JIS           | NVARCHAR2(11 CHAR)  | Yes                                |              | 24        | Customer Jis / 顧客住所コード                                                           |
| CUST_ADR1          | NVARCHAR2(50 CHAR)  | Yes                                |              | 25        | Customer Address1 / 顧客住所1                                                        |
| CUST_ADR2          | NVARCHAR2(50 CHAR)  | Yes                                |              | 26        | Customer Address2 / 顧客住所2                                                        |
| CUST_ADR3          | NVARCHAR2(50 CHAR)  | Yes                                |              | 27        | Customer Address3 / 顧客住所3                                                        |
| CUST_ADR4          | NVARCHAR2(50 CHAR)  | Yes                                |              | 28        | Customer Address4 / 顧客住所4                                                        |
| CUST_ADR5          | NVARCHAR2(50 CHAR)  | Yes                                |              | 29        | Customer Address5 / 顧客住所5                                                        |
| CUST_TEL           | NVARCHAR2(20 CHAR)  | Yes                                |              | 30        | Customer Tel / 顧客電話番号                                                            |
| CUST_ST_KND        | CHAR(1 BYTE)        | Yes                                |              | 31        | Stock Control Kind / 在庫管理区分                                                      |
| CUST_DSP_KND       | CHAR(3 BYTE)        | Yes                                |              | 32        | Display Item Kind / 表示項目区分                                                       |
| CUST_XDOC_FLG      | CHAR(1 BYTE)        | No                                 |              | 33        | Cross Dock Flag / クロスドックフラグ                                                      |
| CUST_STRT_YMD      | DATE                | Yes                                |              | 34        | Business Start Date / 業務開始日                                                      |
| CUST_END_YMD       | DATE                | Yes                                |              | 35        | Business End Date / 業務終了日                                                        |
| CUST_LTDL_FLG      | CHAR(1 BYTE)        | No                                 |              | 36        | Last Shipping or Delivery Control Flag / 最新ピックキー管理フラグ                            |
| CUST_HRET_DAY      | NUMBER(4,0)         | Yes                                |              | 37        | History Retention Days / 履歴保持日数                                                  |
| CUST_CRET_DAY      | NUMBER(4,0)         | Yes                                |              | 38        | Cumulative Retention Days / 累積保持日数                                               |
| CUST_PADS_KND      | CHAR(1 BYTE)        | No                                 |              | 39        | Set Plan Arrival Date to Conf Flag / 入荷確定予定日セットフラグ                               |
| CUST_CIDS_KND      | CHAR(1 BYTE)        | No                                 |              | 40        | Cargo ID Scan Flag / カーゴIDスキャン区分                                                 |
| CUST_PCGP_FLG      | CHAR(1 BYTE)        | No                                 |              | 41        | Controlling the Number of Cargo ID Labels Printed / ピッキングラベル発行枚数制御フラグ            |
| CUST_RSSD_KND      | CHAR(1 BYTE)        | No                                 |              | 42        | Shipping Result Sending Status Kind / 出荷実績送信ステータス区分                              |
| CUST_ABBP_KND      | NVARCHAR2(2 CHAR)   | Yes                                |              | 43        | A-B Branch Point / A-B分岐点                                                        |
| CUST_BCBP_KND      | NVARCHAR2(2 CHAR)   | Yes                                |              | 44        | B-C Branch Point / B-C分岐点                                                        |
| CUST_LOCS_KND      | CHAR(1 BYTE)        | No                                 |              | 45        | Location Search Method Kind / 推奨ロケーション検索区分                                       |
| CUST_AV_AREA       | CHAR(3 BYTE)        | Yes                                |              | 46        | Arrival Default Area / 入荷エリア                                                     |
| CUST_AV_RACK       | NVARCHAR2(10 CHAR)  | Yes                                |              | 47        | Arrival Default Rack / 入荷ラック                                                     |
| CUST_AV_PSTN       | NVARCHAR2(3 CHAR)   | Yes                                |              | 48        | Arrival Default Postion / 入荷ポジション                                                |
| CUST_AV_LVL        | NVARCHAR2(2 CHAR)   | Yes                                |              | 49        | Arrival Default Level / 入荷レベル                                                    |
| CUST_ALBK_AREA     | CHAR(3 BYTE)        | Yes                                |              | 50        | Allocate Back Area / 引当戻しエリア                                                     |
| CUST_ALBK_RACK     | NVARCHAR2(10 CHAR)  | Yes                                |              | 51        | Allocate Back Rack / 引当戻しラック                                                     |
| CUST_ALBK_PSTN     | NVARCHAR2(3 CHAR)   | Yes                                |              | 52        | Allocate Back Position / 引当戻しポジション                                               |
| CUST_ALBK_LVL      | NVARCHAR2(2 CHAR)   | Yes                                |              | 53        | Allocate Back Level / 引当戻しレベル                                                    |
| CUST_KITP_AREA     | CHAR(3 BYTE)        | Yes                                |              | 54        | Kitting Process Area / 流通加工エリア                                                   |
| CUST_KITP_RACK     | NVARCHAR2(10 CHAR)  | Yes                                |              | 55        | Kitting Process Rack / 流通加工ラック                                                   |
| CUST_KITP_PSTN     | NVARCHAR2(3 CHAR)   | Yes                                |              | 56        | Kitting Process Position / 流通加工ポジション                                             |
| CUST_KITP_LVL      | NVARCHAR2(2 CHAR)   | Yes                                |              | 57        | Kitting Process Level / 流通加工レベル                                                  |
| CUST_UREP_KND      | CHAR(1 BYTE)        | No                                 |              | 58        | Urgent Replenishment Kind / 緊急補充区分                                               |
| CUST_UREP_FLG      | CHAR(1 BYTE)        | No                                 |              | 59        | Urgent Replenishment Location Flag / 緊急補充先ロケ使用フラグ                                |
| CUST_UREP_AREA     | CHAR(3 BYTE)        | Yes                                |              | 60        | Urgent Replenishment Area / 緊急補充先エリア                                             |
| CUST_UREP_RACK     | NVARCHAR2(10 CHAR)  | Yes                                |              | 61        | Urgent Replenishment Rack / 緊急補充先ラック                                             |
| CUST_UREP_PSTN     | NVARCHAR2(3 CHAR)   | Yes                                |              | 62        | Urgent Replenishment Position / 緊急補充先ポジション                                       |
| CUST_UREP_LVL      | NVARCHAR2(2 CHAR)   | Yes                                |              | 63        | Urgent Replenishment Level / 緊急補充先レベル                                            |
| CUST_AIQP_FLG      | CHAR(1 BYTE)        | No                                 |              | 64        | Arrival Inspection Quantity Input Flag / 入荷検品数値入力不可フラグ                           |
| CUST_PKQP_FLG      | CHAR(1 BYTE)        | No                                 |              | 65        | Picking Quantity Input Prihibited Flag / ピッキング数値入力不可フラグ                          |
| CUST_IVQP_FLG      | CHAR(1 BYTE)        | No                                 |              | 66        | Inventory Quantity Input Prihibited Flag / 棚卸数値入力不可フラグ                           |
| CUST_RIQP_FLG      | CHAR(1 BYTE)        | No                                 |              | 67        | Return Goods Arrival Inspection Quantity Input Prihibited Flag / 返品入荷検品数値入力不可フラグ |
| CUST_SIQP_FLG      | CHAR(1 BYTE)        | No                                 |              | 68        | Shipping Inspection Quantity Input Prihibited Flag / 出荷検品数値入力不可フラグ               |
| CUST_AIVP_FLG      | CHAR(1 BYTE)        | No                                 |              | 69        | Arrival Invoice No Input Prohibited Flag / 入荷伝票番号重複入力不可フラグ                       |
| CUST_ARFP_FLG      | CHAR(1 BYTE)        | No                                 |              | 70        | Arrival Refarence No Input Prohibited Flag / 入荷リファレンスNo重複入力不可フラグ                 |
| CUST_APOP_FLG      | CHAR(1 BYTE)        | No                                 |              | 71        | Arrival P/O No Input Prohibited Flag / 入荷P/O No重複入力不可フラグ                         |
| CUST_SIVP_FLG      | CHAR(1 BYTE)        | No                                 |              | 72        | Shipping Invoice No Input Prohibited Flag / 出荷伝票番号重複入力不可フラグ                      |
| CUST_SRFP_FLG      | CHAR(1 BYTE)        | No                                 |              | 73        | Shipping Refarence No Input Prohibited Flag / 出荷リファレンスNo重複入力不可フラグ                |
| CUST_SPOP_FLG      | CHAR(1 BYTE)        | No                                 |              | 74        | Shipping P/O No Input Prohibited Flag / 出荷P/O No重複入力不可フラグ                        |
| CUST_DAVT_KND      | CHAR(3 BYTE)        | Yes                                |              | 75        | Default Arrival Transaction Kind / 入荷伝票区分 初期値                                    |
| CUST_DUIT_KND      | CHAR(3 BYTE)        | Yes                                |              | 76        | Default Unplan In Transaction Kind / 入庫伝票区分 初期値                                  |
| CUST_DSPT_KND      | CHAR(3 BYTE)        | Yes                                |              | 77        | Default Shipping Transaction Kind / 出荷伝票区分 初期値                                   |
| CUST_DUOT_KND      | CHAR(3 BYTE)        | Yes                                |              | 78        | Default Unplan Out Transaction Kind / 出庫伝票区分 初期値                                 |
| CUST_DSJT_KND      | CHAR(3 BYTE)        | Yes                                |              | 79        | Default Stock Adjust Transaction Kind / 在庫調整区分 初期値                               |
| CUST_DRLT_KND      | CHAR(3 BYTE)        | Yes                                |              | 80        | Default Re-location Transaction Kind / 棚振伝票区分 初期値                                |
| CUST_DPTT_KND      | CHAR(3 BYTE)        | Yes                                |              | 81        | Default Product Transfer Transaction Kind / 商振伝票区分 初期値                           |
| CUST_DKAT_KND      | CHAR(3 BYTE)        | Yes                                |              | 82        | Default Kitting Arrival Transaction Kind / 組立入庫伝票区分 初期値                          |
| CUST_DKST_KND      | CHAR(3 BYTE)        | Yes                                |              | 83        | Default Kitting Shipping Transaction Kind / 組立出庫伝票区分 初期値                         |
| CUST_DBAT_KND      | CHAR(3 BYTE)        | Yes                                |              | 84        | Default Break Arrival Transaction Kind / 解体入庫伝票区分 初期値                            |
| CUST_DBST_KND      | CHAR(3 BYTE)        | Yes                                |              | 85        | Default Break Shipping Transaction Kind / 解体出庫伝票区分 初期値                           |
| CUST_SHPR_COD      | NVARCHAR2(40 CHAR)  | Yes                                |              | 86        | Shipper Code / 荷送人コード                                                            |
| CUST_SHPR_NAM1     | NVARCHAR2(50 CHAR)  | Yes                                |              | 87        | Shipper Name1st / 荷送人名称1st                                                       |
| CUST_SHPR_NAM2     | NVARCHAR2(50 CHAR)  | Yes                                |              | 88        | Shipper Name2nd / 荷送人名称2nd                                                       |
| CUST_SHPR_TEL      | NVARCHAR2(20 CHAR)  | Yes                                |              | 89        | Shipper Tel / 荷送人電話番号                                                            |
| CUST_SHPR_ZIP      | NVARCHAR2(10 CHAR)  | Yes                                |              | 90        | Shipper Zip / 荷送人郵便番号                                                            |
| CUST_SHPR_JIS      | NVARCHAR2(11 CHAR)  | Yes                                |              | 91        | Shipper Jis / 荷送人住所コード                                                           |
| CUST_SHPR_ADR1     | NVARCHAR2(50 CHAR)  | Yes                                |              | 92        | Shipper Address1 / 荷送人住所1                                                        |
| CUST_SHPR_ADR2     | NVARCHAR2(50 CHAR)  | Yes                                |              | 93        | Shipper Address2 / 荷送人住所2                                                        |
| CUST_SHPR_ADR3     | NVARCHAR2(50 CHAR)  | Yes                                |              | 94        | Shipper Address3 / 荷送人住所3                                                        |
| CUST_SHPR_ADR4     | NVARCHAR2(50 CHAR)  | Yes                                |              | 95        | Shipper Address4 / 荷送人住所4                                                        |
| CUST_SHPR_ADR5     | NVARCHAR2(50 CHAR)  | Yes                                |              | 96        | Shipper Address5 / 荷送人住所5                                                        |
| CUST_SHPD_NAM      | NVARCHAR2(50 CHAR)  | Yes                                |              | 97        | Shipper Department Name / 荷送人部署名                                                 |
| CUST_REP_NAM       | NVARCHAR2(50 CHAR)  | Yes                                |              | 98        | Shipper Representative / 荷送人宛先担当者                                                |
| CUST_SHPR_MAL      | NVARCHAR2(100 CHAR) | Yes                                |              | 99        | Supplier Mail / 荷送人メールアドレス                                                       |
| CUST_SVAT_COD      | CHAR(2 BYTE)        | Yes                                |              | 100       | Vat Registration Country / 荷送人_VAT_REGISTRATION_COUNTRY                          |
| CUST_SVAT_NUM      | CHAR(15 BYTE)       | Yes                                |              | 101       | Vat Registration No / 荷送人_VAT_REGISTRATION_NO.                                   |
| CUST_ALGP_FLG      | CHAR(1 BYTE)        | No                                 |              | 102       | Allocate Grouping Flag / 引当集約フラグ                                                 |
| CUST_COPA_FLG      | CHAR(1 BYTE)        | No                                 |              | 103       | Piece Allocate When Case Stock-Out Flag / ケース欠品時バラ品引当許可フラグ                       |
| CUST_ALPR_KND      | CHAR(2 BYTE)        | No                                 |              | 104       | Allocate Priority Kind / 出荷オーダー優先引当順位区分                                          |
| CUST_GET_KND       | CHAR(1 BYTE)        | No                                 |              | 105       | Serial/LOT Get Kind / シリアル/LOT取得フラグ                                              |
| CUST_SBIV_FLG      | CHAR(1 BYTE)        | No                                 |              | 106       | Sub-inventry Usage Flag / 等級使用フラグ                                                |
| CUST_OPD_FLG       | CHAR(1 BYTE)        | No                                 |              | 107       | Operation Date Usage Flag / 運用日使用フラグ                                             |
| CUST_ALP_FLG       | CHAR(1 BYTE)        | No                                 |              | 108       | Allocation Location Priority Flag / 引当時ロケーション優先フラグ                               |
| CUST_BARL_KIND     | CHAR(1 BYTE)        | Yes                                |              | 109       | Barcode Length Kind / BCD固定長可変長区分                                                |
| CUST_STRP          | NUMBER(3,0)         | Yes                                |              | 110       | Start Position(Fixed) / 開始位置(固定)                                                 |
| CUST_LENF          | NUMBER(3,0)         | Yes                                |              | 111       | Length(Fixed) / 桁数(固定)                                                           |
| CUST_SPRT          | NVARCHAR2(3 CHAR)   | Yes                                |              | 112       | separator(Variable) / セパレーター(可変)                                                 |
| CUST_POSV          | NUMBER(3,0)         | Yes                                |              | 113       | Column(Variable) / カラム位置(可変)                                                     |
| CUST_SWP_FLG       | CHAR(1 BYTE)        | No                                 |              | 114       | Swapping Flag / スワッピング可否フラグ                                                      |
| CUST_BTG_FLG       | CHAR(1 BYTE)        | No                                 | "'N'         |
| "                  | 115                 | Batch group Usage Flag / バッチ組使用フラグ |
| CUST_PAR_FLG       | CHAR(1 BYTE)        | No                                 | N            | 116       | ProductMaster AutoReg Flag / 新規商品自動登録フラグ                                         |
| CUST_RTRN_AREA     | CHAR(3 BYTE)        | Yes                                |              | 117       | Return Goods Area / 返品エリア                                                        |
| CUST_RTRN_RACK     | NVARCHAR2(10 CHAR)  | Yes                                |              | 118       | Return Goods Rack / 返品ラック                                                        |
| CUST_RTRN_PSTN     | NVARCHAR2(3 CHAR)   | Yes                                |              | 119       | Return Goods Position / 返品ポジション                                                  |
| CUST_RTRN_LVL      | NVARCHAR2(2 CHAR)   | Yes                                |              | 120       | Return Goods Level / 返品レベル                                                       |
| CUST_EXCP_AREA     | CHAR(3 BYTE)        | Yes                                |              | 121       | Exception Area / 例外エリア                                                           |
| CUST_EXCP_RACK     | NVARCHAR2(10 CHAR)  | Yes                                |              | 122       | Exception Rack / 例外ラック                                                           |
| CUST_EXCP_PSTN     | NVARCHAR2(3 CHAR)   | Yes                                |              | 123       | Exception Position / 例外ポジション                                                     |
| CUST_EXCP_LVL      | NVARCHAR2(2 CHAR)   | Yes                                |              | 124       | Exception Level / 例外レベル                                                          |
| CUST_DVINS_KND     | CHAR(1 BYTE)        | No                                 | 0            | 125       | Delivery Insurance Amount Calculation Kind / 配送保険金額計算区分                          |
| CUST_DVINS_AMT     | NUMBER(14,4)        | Yes                                |              | 126       | Delivery Insurance Amount / 配送保険金額                                               |
| CUST_HTMN_COD      | NVARCHAR2(100 CHAR) | Yes                                |              | 127       | HHT Menu ID / HHTメニューID                                                          |
| CUST_KPIJ_KND      | CHAR(3 BYTE)        | No                                 | 000          | 128       | KPI Job Control Kind / KPIジョブ稼働管理区分                                              |
| CUST_DVGEN_FLG     | CHAR(1 BYTE)        | No                                 | N            | 129       | Delivery Information Generate Flg / 配送情報作成フラグ                                    |
| CUST_CCAV_FLG      | CHAR(1 BYTE)        | No                                 | N            | 130       | Cancel Arrival Confirmation Flag / 入荷確定キャンセルフラグ                                  |
| CUST_CCSP_FLG      | CHAR(1 BYTE)        | No                                 | N            | 131       | Cancel Shipping Confirmation Flag / 出荷確定キャンセルフラグ                                 |
| CUST_LDQP_FLG      | CHAR(1 BYTE)        | No                                 | N            | 132       | Loading Quantity Input Prohibited Flag/積載数量入力禁止フラグ                               |
| CUST_PCSM_FLG      | CHAR(1 BYTE)        | No                                 | N            | 133       | Piece Management Flag/ピース管理フラグ                                                   |
| CUST_SPLM_FLG      | CHAR(1 BYTE)        | No                                 | Y            | 134       | Supplier Master Management Flag/納入元マスタ管理フラグ                                      |
| CUST_DLVM_FLG      | CHAR(1 BYTE)        | No                                 | Y            | 135       | Delivery Master Management flag/配送先マスタ管理フラグ                                      |
| CUST_DBAR_KND      | NVARCHAR2(4 CHAR)   | Yes                                |              | 136       | Default Barcode Type / 初期バーコードタイプ                                                |
| CUST_ATAI_KND      | NVARCHAR2(2 CHAR)   | Yes                                |              | 137       | Auto-Pick Arrival No. Kind in Arrival Inspection / 入荷検品入荷管理番号自動取得区分              |
| CUST_ATSI_KND      | NVARCHAR2(2 CHAR)   | Yes                                |              | 138       | Auto-Pick Shipping No. Kind in Shipping Inspection / 出荷検品出荷管理番号自動取得区分            |
| CUST_LCQP_FLG      | CHAR(1 BYTE)        | No                                 | N            | 139       | Location Entry Quantity Input Prihibited Flag / 棚付数値入力不可フラグ                      |
| CUST_SCAI_KND      | NVARCHAR2(2 CHAR)   | No                                 | 05           | 140       | Shape control in Arrival Inspection / 入荷検品荷姿制御区分                                 |
| CUST_SCPK_KND      | NVARCHAR2(2 CHAR)   | No                                 | 05           | 141       | Shape control in Picking / ピッキング荷姿制御区分                                           |
| CUST_SCSI_KND      | NVARCHAR2(2 CHAR)   | No                                 | 05           | 142       | Shape control in Shipping Inspection / 出荷検品荷姿制御区分                                |
| CUST_SCST_KND      | NVARCHAR2(2 CHAR)   | No                                 | 05           | 143       | Shape control in Storage / 庫内作業荷姿制御区分                                            |
| CUST_SCLC_KND      | NVARCHAR2(2 CHAR)   | No                                 | 05           | 144       | Shape control in Location Entry / 棚付荷姿制御区分                                       |
| CUST_WEND_SUN_FLG  | CHAR(1 BYTE)        | No                                 | N            | 145       | Weekend Setting FLG(Sun) / 週末設定FLG(日曜日)                                          |
| CUST_WEND_MON_FLG  | CHAR(1 BYTE)        | No                                 | N            | 146       | Weekend Setting FLG(Mon) / 週末設定FLG(月曜日)                                          |
| CUST_WEND_TUE_FLG  | CHAR(1 BYTE)        | No                                 | N            | 147       | Weekend Setting FLG(Tue) / 週末設定FLG(火曜日)                                          |
| CUST_WEND_WED_FLG  | CHAR(1 BYTE)        | No                                 | N            | 148       | Weekend Setting FLG(Wed) / 週末設定FLG(水曜日)                                          |
| CUST_WEND_THU_FLG  | CHAR(1 BYTE)        | No                                 | N            | 149       | Weekend Setting FLG(Thu) / 週末設定FLG(木曜日)                                          |
| CUST_WEND_FRI_FLG  | CHAR(1 BYTE)        | No                                 | N            | 150       | Weekend Setting FLG(Fri) / 週末設定FLG(金曜日)                                          |
| CUST_WEND_SAT_FLG  | CHAR(1 BYTE)        | No                                 | N            | 151       | Weekend Setting FLG(Sat) / 週末設定FLG(土曜日)                                          |
| CUST_SHPR_CTRY_COD | NVARCHAR2(2 CHAR)   | Yes                                |              | 152       | Shipper Country Code / 荷送人国コード                                                   |
| CUST_SHPR_CITY_COD | NVARCHAR2(3 CHAR)   | Yes                                |              | 153       | Shipper City Code / 荷送人都市コード                                                     |
| CUST_SHPR_CITY_NAM | NVARCHAR2(50 CHAR)  | Yes                                |              | 154       | Shipper City Name / 荷送人都市名                                                       |
| CUST_SHPR_STAT_COD | NVARCHAR2(2 CHAR)   | Yes                                |              | 155       | Shipper State Code / 荷送人州コード                                                     |
| CUST_STLR_KEY1     | NVARCHAR2(50 CHAR)  | Yes                                |              | 156       | Report Sorting List by sequence Key1                                             |
| CUST_STLR_KEY2     | NVARCHAR2(50 CHAR)  | Yes                                |              | 157       | Report Sorting List by sequence Key2                                             |
| CUST_STLR_KEY3     | NVARCHAR2(50 CHAR)  | Yes                                |              | 158       | Report Sorting List by sequence Key3                                             |
| CUST_STLR_KEY4     | NVARCHAR2(50 CHAR)  | Yes                                |              | 159       | Report Sorting List by sequence Key4                                             |
| CUST_STLR_KEY5     | NVARCHAR2(50 CHAR)  | Yes                                |              | 160       | Report Sorting List by sequence Key5                                             |
| CUST_STLR_KEY6     | NVARCHAR2(50 CHAR)  | Yes                                |              | 161       | Report Sorting List by sequence Key6                                             |
| CUST_STLR_KEY7     | NVARCHAR2(50 CHAR)  | Yes                                |              | 162       | Report Sorting List by sequence Key7                                             |
| CUST_STLR_KEY8     | NVARCHAR2(50 CHAR)  | Yes                                |              | 163       | Report Sorting List by sequence Key8                                             |
| CUST_ASLR_KEY1     | NVARCHAR2(50 CHAR)  | Yes                                |              | 164       | Report Assorting List by sequence Key1                                           |
| CUST_ASLR_KEY2     | NVARCHAR2(50 CHAR)  | Yes                                |              | 165       | Report Assorting List by sequence Key2                                           |
| CUST_ASLR_KEY3     | NVARCHAR2(50 CHAR)  | Yes                                |              | 166       | Report Assorting List by sequence Key3                                           |
| CUST_ASLR_KEY4     | NVARCHAR2(50 CHAR)  | Yes                                |              | 167       | Report Assorting List by sequence Key4                                           |
| CUST_ASLR_KEY5     | NVARCHAR2(50 CHAR)  | Yes                                |              | 168       | Report Assorting List by sequence Key5                                           |
| CUST_ASLR_KEY6     | NVARCHAR2(50 CHAR)  | Yes                                |              | 169       | Report Assorting List by sequence Key6                                           |
| CUST_ASLR_KEY7     | NVARCHAR2(50 CHAR)  | Yes                                |              | 170       | Report Assorting List by sequence Key7                                           |
| CUST_ASLR_KEY8     | NVARCHAR2(50 CHAR)  | Yes                                |              | 171       | Report Assorting List by sequence Key8                                           |
| CUST_PLT_CTL_KND   | NVARCHAR2(2 CHAR)   | No                                 | 00           | 172       | Palletize control Kind / パレタイズ制御区分                                               |
| CUST_PSDS_KND      | CHAR(1 BYTE)        | No                                 | N            | 173       | Set Plan Shipping Date to Conf Flag / 出荷確定予定日セットフラグ                              |
| CUST_ROTP_KND      | NVARCHAR2(3 CHAR)   | Yes                                |              | 174       | Rollout Type Kind / 移行区分                                                         |
| CUST_CFG1_FLG      | CHAR(1 BYTE)        | No                                 | N            | 175       | Customized Flag 01 / 顧客専用フラグ01                                                   |
| CUST_CFG2_FLG      | CHAR(1 BYTE)        | No                                 | N            | 176       | Customized Flag 02 / 顧客専用フラグ02                                                   |
| CUST_CFG3_FLG      | CHAR(1 BYTE)        | No                                 | N            | 177       | Customized Flag 03 / 顧客専用フラグ03                                                   |
| CUST_CFG4_FLG      | CHAR(1 BYTE)        | No                                 | N            | 178       | Customized Flag 04 / 顧客専用フラグ04                                                   |
| CUST_CFG5_FLG      | CHAR(1 BYTE)        | No                                 | N            | 179       | Customized Flag 05 / 顧客専用フラグ05                                                   |
| CUST_CFG6_FLG      | CHAR(1 BYTE)        | No                                 | N            | 180       | Customized Flag 06 / 顧客専用フラグ06                                                   |
| CUST_CFG7_FLG      | CHAR(1 BYTE)        | No                                 | N            | 181       | Customized Flag 07 / 顧客専用フラグ07                                                   |
| CUST_CFG8_FLG      | CHAR(1 BYTE)        | No                                 | N            | 182       | Customized Flag 08 / 顧客専用フラグ08                                                   |
| CUST_CFG9_FLG      | CHAR(1 BYTE)        | No                                 | N            | 183       | Customized Flag 09 / 顧客専用フラグ09                                                   |
| CUST_CFG10_FLG     | CHAR(1 BYTE)        | No                                 | N            | 184       | Customized Flag 10 / 顧客専用フラグ10                                                   |
| CUST_CFG11_FLG     | CHAR(1 BYTE)        | No                                 | N            | 185       | Customized Flag 11 / 顧客専用フラグ11                                                   |
| CUST_CFG12_FLG     | CHAR(1 BYTE)        | No                                 | N            | 186       | Customized Flag 12 / 顧客専用フラグ12                                                   |
| CUST_CFG13_FLG     | CHAR(1 BYTE)        | No                                 | N            | 187       | Customized Flag 13 / 顧客専用フラグ13                                                   |
| CUST_CFG14_FLG     | CHAR(1 BYTE)        | No                                 | N            | 188       | Customized Flag 14 / 顧客専用フラグ14                                                   |
| CUST_CFG15_FLG     | CHAR(1 BYTE)        | No                                 | N            | 189       | Customized Flag 15 / 顧客専用フラグ15                                                   |
| CUST_CNI1_NUM      | NUMBER(9,0)         | Yes                                |              | 190       | Customized Number Item 01 / 顧客専用数字項目01                                           |
| CUST_CNI2_NUM      | NUMBER(9,0)         | Yes                                |              | 191       | Customized Number Item 02 / 顧客専用数字項目02                                           |
| CUST_CNI3_NUM      | NUMBER(9,0)         | Yes                                |              | 192       | Customized Number Item 03 / 顧客専用数字項目03                                           |
| CUST_CNI4_NUM      | NUMBER(9,0)         | Yes                                |              | 193       | Customized Number Item 04 / 顧客専用数字項目04                                           |
| CUST_CNI5_NUM      | NUMBER(9,0)         | Yes                                |              | 194       | Customized Number Item 05 / 顧客専用数字項目05                                           |
| CUST_CNI6_NUM      | NUMBER(9,0)         | Yes                                |              | 195       | Customized Number Item 06 / 顧客専用数字項目06                                           |
| CUST_CNI7_NUM      | NUMBER(9,0)         | Yes                                |              | 196       | Customized Number Item 07 / 顧客専用数字項目07                                           |
| CUST_CNI8_NUM      | NUMBER(9,0)         | Yes                                |              | 197       | Customized Number Item 08 / 顧客専用数字項目08                                           |
| CUST_CNI9_NUM      | NUMBER(9,0)         | Yes                                |              | 198       | Customized Number Item 09 / 顧客専用数字項目09                                           |
| CUST_CNI10_NUM     | NUMBER(14,4)        | Yes                                |              | 199       | Customized Number Item 10 / 顧客専用数字項目10                                           |
| CUST_CNI11_NUM     | NUMBER(14,4)        | Yes                                |              | 200       | Customized Number Item 11 / 顧客専用数字項目11                                           |
| CUST_CNI12_NUM     | NUMBER(14,4)        | Yes                                |              | 201       | Customized Number Item 12 / 顧客専用数字項目12                                           |
| CUST_CNI13_NUM     | NUMBER(14,4)        | Yes                                |              | 202       | Customized Number Item 13 / 顧客専用数字項目13                                           |
| CUST_CNI14_NUM     | NUMBER(14,4)        | Yes                                |              | 203       | Customized Number Item 14 / 顧客専用数字項目14                                           |
| CUST_CNI15_NUM     | NUMBER(14,4)        | Yes                                |              | 204       | Customized Number Item 15 / 顧客専用数字項目15                                           |
| CUST_CIT1          | NVARCHAR2(100 CHAR) | Yes                                |              | 205       | Customized Item 01 / 顧客専用項目01                                                    |
| CUST_CIT2          | NVARCHAR2(100 CHAR) | Yes                                |              | 206       | Customized Item 02 / 顧客専用項目02                                                    |
| CUST_CIT3          | NVARCHAR2(100 CHAR) | Yes                                |              | 207       | Customized Item 03 / 顧客専用項目03                                                    |
| CUST_CIT4          | NVARCHAR2(100 CHAR) | Yes                                |              | 208       | Customized Item 04 / 顧客専用項目04                                                    |
| CUST_CIT5          | NVARCHAR2(100 CHAR) | Yes                                |              | 209       | Customized Item 05 / 顧客専用項目05                                                    |
| CUST_CIT6          | NVARCHAR2(100 CHAR) | Yes                                |              | 210       | Customized Item 06 / 顧客専用項目06                                                    |
| CUST_CIT7          | NVARCHAR2(100 CHAR) | Yes                                |              | 211       | Customized Item 07 / 顧客専用項目07                                                    |
| CUST_CIT8          | NVARCHAR2(100 CHAR) | Yes                                |              | 212       | Customized Item 08 / 顧客専用項目08                                                    |
| CUST_CIT9          | NVARCHAR2(100 CHAR) | Yes                                |              | 213       | Customized Item 09 / 顧客専用項目09                                                    |
| CUST_CIT10         | NVARCHAR2(100 CHAR) | Yes                                |              | 214       | Customized Item 10 / 顧客専用項目10                                                    |
| CUST_CIT11         | NVARCHAR2(100 CHAR) | Yes                                |              | 215       | Customized Item 11 / 顧客専用項目11                                                    |
| CUST_CIT12         | NVARCHAR2(100 CHAR) | Yes                                |              | 216       | Customized Item 12 / 顧客専用項目12                                                    |
| CUST_CIT13         | NVARCHAR2(100 CHAR) | Yes                                |              | 217       | Customized Item 13 / 顧客専用項目13                                                    |
| CUST_CIT14         | NVARCHAR2(100 CHAR) | Yes                                |              | 218       | Customized Item 14 / 顧客専用項目14                                                    |
| CUST_CIT15         | NVARCHAR2(100 CHAR) | Yes                                |              | 219       | Customized Item 15 / 顧客専用項目15                                                    |
| CUST_DCAR_KEY1     | NVARCHAR2(50 CHAR)  | Yes                                |              | 220       | Report Document Arrival by sequence Key1                                         |
| CUST_DCAR_KEY2     | NVARCHAR2(50 CHAR)  | Yes                                |              | 221       | Report Document Arrival by sequence Key2                                         |
| CUST_DCAR_KEY3     | NVARCHAR2(50 CHAR)  | Yes                                |              | 222       | Report Document Arrival by sequence Key3                                         |
| CUST_DCAR_KEY4     | NVARCHAR2(50 CHAR)  | Yes                                |              | 223       | Report Document Arrival by sequence Key4                                         |
| CUST_DCAR_KEY5     | NVARCHAR2(50 CHAR)  | Yes                                |              | 224       | Report Document Arrival by sequence Key5                                         |
| CUST_DCAR_KEY6     | NVARCHAR2(50 CHAR)  | Yes                                |              | 225       | Report Document Arrival by sequence Key6                                         |
| CUST_DCAR_KEY7     | NVARCHAR2(50 CHAR)  | Yes                                |              | 226       | Report Document Arrival by sequence Key7                                         |
| CUST_DCAR_KEY8     | NVARCHAR2(50 CHAR)  | Yes                                |              | 227       | Report Document Arrival by sequence Key8                                         |
| CUST_YMLK_FLG      | CHAR(1 BYTE)        | No                                 | N            | 228       | YAMATO Linkage Flag / ヤマト連携フラグ                                                   |
| CUST_ALPR_PIK_KND  | CHAR(1 BYTE)        | No                                 | 0            | 229       | Allocate Priority Pik Key Kind / 出荷オーダー優先引当PIKキー区分                               |
| CUST_PCER_FLG      | CHAR(1 BYTE)        | No                                 | N            | 230       | Show Error Pop-up flag for PC Packing / PC梱包エラー時ポップアップ表示フラグ                      |
| CUST_ACAR_FLG      | CHAR(1 BYTE)        | No                                 | N            | 231       | Auto Generate Carton ID Flag / カートン ID フラグの自動生成                                  |
| CUST_SRQP_FLG      | CHAR(1 BYTE)        | No                                 | N            | 232       | Sorting Inspection Quantity Input Flag / 仕分検品数値入力不可フラグ                           |
| CUST_CIDO_FLG      | CHAR(1 BYTE)        | No                                 | N            | 233       | Cargo ID label output during inspection flag / 検品時カーゴIDラベル出力フラグ                  |
| CUST_SCSR_KND      | NVARCHAR2(2 CHAR)   | No                                 | 05           | 234       | Shape control in Sorting / 仕分荷姿制御区分                                              |
| CUST_SRLU_FLG      | CHAR(1 BYTE)        | No                                 | N            | 235       | Serial Unique Flag / シリアルユニークフラグ                                                 |
| CUST_SBIV_OVR_FLG  | CHAR(1 BYTE)        | No                                 | N            | 236       | Sub Inventory Override Flag / サブインベントリ上書きフラグ                                     |
| CUST_PLT_ACAR_FLG  | CHAR(1 BYTE)        | No                                 | N            | 237       | Auto Generate Carton ID Flag for Palletizing / パレタイズ時カートンID自動印刷フラグ               |
| CUST_KPI_FLG       | CHAR(1 BYTE)        | No                                 | N            | 238       | KPI work log output flag / KPI作業ログ出力フラグ                                          |
| CUST_VER_NUM       | NUMBER(6,0)         | Yes                                |              | 239       | Template Version / テンプレートバージョン                                                   |
| CUST_APPC_FLG      | CHAR(1 BYTE)        | No                                 | N            | 240       | Allow Product PCS/CS Change Flag/ 入数変更許可フラグ                                      |
| CUST_DAR_FLG       | CHAR(1 BYTE)        | No                                 | N            | 241       | DeliveryToMaster AutoReg Flag / 新規配送先自動登録フラグ                                     |
