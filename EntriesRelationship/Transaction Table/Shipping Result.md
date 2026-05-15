# GWH_TJ_SP_R
#Entity #Standard #OUTBOUND 

| COLUMN_NAME   | DATA_TYPE           | NULLABLE                   | DATA_DEFAULT | COLUMN_ID | COMMENTS                                          |
|---------------|---------------------|----------------------------|--------------|-----------|---------------------------------------------------|
| DEL_FLG       | CHAR(1 BYTE)        | No                         |              | 1         | Delete Flag / 論理削除フラグ                             |
| CRT_YMD       | CHAR(8 BYTE)        | No                         |              | 2         | Create Date (UTC) / 作成日(標準時)                      |
| CRT_TIM       | CHAR(6 BYTE)        | No                         |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                     |
| CRT_TMID      | NVARCHAR2(100 CHAR) | No                         |              | 4         | Create Term Id / 作成端末ID                           |
| CRT_USER      | NVARCHAR2(50 CHAR)  | No                         |              | 5         | Create User Id / 作成ユーザID                          |
| CRT_PGM       | NVARCHAR2(30 CHAR)  | No                         |              | 6         | Create Program Id / 作成プログラムID                     |
| CRT_TM_ZONE   | NVARCHAR2(3 CHAR)   | No                         |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン               |
| CRT_YMDHMS    | TIMESTAMP(6)        | No                         |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）          |
| CRT_L_YMDHMS  | TIMESTAMP(6)        | No                         |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）       |
| UPD_YMD       | CHAR(8 BYTE)        | No                         |              | 10        | Update Date (UTC) / 更新日(標準時)                      |
| UPD_TIM       | CHAR(6 BYTE)        | No                         |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                     |
| UPD_TMID      | NVARCHAR2(100 CHAR) | No                         |              | 12        | Update Terminal ID / 更新端末ID                       |
| UPD_USER      | NVARCHAR2(50 CHAR)  | No                         |              | 13        | Update User ID / 更新ユーザID                          |
| UPD_PGM       | NVARCHAR2(30 CHAR)  | No                         |              | 14        | Update Program ID / 更新プログラムID                     |
| UPD_TM_ZONE   | NVARCHAR2(3 CHAR)   | No                         |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン               |
| UPD_YMDHMS    | TIMESTAMP(6)        | No                         |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）          |
| UPD_L_YMDHMS  | TIMESTAMP(6)        | No                         |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）       |
| SPR_CPNY_COD  | NVARCHAR2(6 CHAR)   | No                         |              | 18        | Company Code / カンパニーコード                           |
| SPR_WHS_COD   | NVARCHAR2(4 CHAR)   | No                         |              | 19        | Warehouse Code / 倉庫コード                            |
| SPR_CUST_COD  | NVARCHAR2(13 CHAR)  | No                         |              | 20        | Customer Code / 顧客コード                             |
| SPR_AS_NUM    | NVARCHAR2(6 CHAR)   | No                         |              | 21        | Arrival and Shipping Number / 入出荷番号               |
| SPR_ASLN_NUM  | NUMBER(4,0)         | No                         |              | 22        | Arrival and Shipping Line Number / 入出荷ラインNo       |
| SPR_ASSQ_NUM  | NUMBER(4,0)         | No                         |              | 23        | Arrival and Shipping Seq Number / 入出荷SEQNo        |
| SPR_AS_KND    | NVARCHAR2(2 CHAR)   | No                         |              | 24        | Arrival and Shipping Kind / 入出荷区分                 |
| SPR_TRN_KND   | NVARCHAR2(3 CHAR)   | No                         |              | 25        | Transaction Kind / 伝票区分                           |
| SPR_CRT_YMD   | DATE                | No                         |              | 26        | Create Date / レコード作成日                             |
| SPR_AVSP_YMD  | DATE                | Yes                        |              | 27        | Arrived and Shipped Date / 入出荷日                   |
| SPR_AVSP_STS  | NVARCHAR2(3 CHAR)   | No                         |              | 28        | Arrival and Shipping Status / 入出荷ステータス            |
| SPR_PROD_COD  | NVARCHAR2(50 CHAR)  | No                         |              | 29        | Product Code / 商品コード                              |
| SPR_ORGN_COD  | CHAR(2 BYTE)        | Yes                        |              | 30        | Country Of Origin / 原産国                           |
| SPR_PPCS_QTY  | NUMBER(9,0)         | No                         |              | 31        | Number Of Pieces Per Case / ケース入数                 |
| SPR_RCS_QTY   | NUMBER(9,0)         | No                         |              | 32        | Case QTY(Result.) / ケース実績数                        |
| SPR_RPC_QTY   | NUMBER(12,3)        | No                         |              | 33        | Piece QTY(Result.) / バラ実績数                        |
| SPR_RTPC_QTY  | NUMBER(12,3)        | No                         |              | 34        | Total Piece QTY(Result.) / 総バラ実績数                 |
| SPR_AREA_COD  | CHAR(3 BYTE)        | Yes                        |              | 35        | Area / エリア                                        |
| SPR_RACK_COD  | NVARCHAR2(10 CHAR)  | Yes                        |              | 36        | Rack / ラック                                        |
| SPR_PSTN_COD  | NVARCHAR2(3 CHAR)   | Yes                        |              | 37        | Position / ポジション                                  |
| SPR_LVL_COD   | NVARCHAR2(2 CHAR)   | Yes                        |              | 38        | Level / レベル                                       |
| SPR_SBIV_COD  | NVARCHAR2(20 CHAR)  | No                         |              | 39        | Sub Inventry / 等級コード                              |
| SPR_PIK1      | NVARCHAR2(30 CHAR)  | Yes                        |              | 40        | PICKING KEY1 / ピッキングキー1                           |
| SPR_PIK2      | NVARCHAR2(30 CHAR)  | Yes                        |              | 41        | PICKING KEY2 / ピッキングキー2                           |
| SPR_PIK3      | NVARCHAR2(30 CHAR)  | Yes                        |              | 42        | PICKING KEY3 / ピッキングキー3                           |
| SPR_PIK4      | NVARCHAR2(30 CHAR)  | Yes                        |              | 43        | PICKING KEY4 / ピッキングキー4                           |
| SPR_PIK5      | NVARCHAR2(30 CHAR)  | Yes                        |              | 44        | PICKING KEY5 / ピッキングキー5                           |
| SPR_PIK6      | NVARCHAR2(30 CHAR)  | Yes                        |              | 45        | PICKING KEY6 / ピッキングキー6                           |
| SPR_PIK7      | NVARCHAR2(30 CHAR)  | Yes                        |              | 46        | PICKING KEY7 / ピッキングキー7                           |
| SPR_AV_NUM    | NVARCHAR2(6 CHAR)   | Yes                        |              | 47        | Arrival Number / 入荷番号                             |
| SPR_AVLN_NUM  | NUMBER(4,0)         | Yes                        |              | 48        | Arrival Line No / 入荷ラインNo                         |
| SPR_AVSQ_NUM  | NUMBER(4,0)         | Yes                        |              | 49        | Arrival Seq No / 入荷SEQNo                          |
| SPR_DMG_FLG   | CHAR(1 BYTE)        | No                         |              | 50        | Damage/Hold Flag / ダメージ/ホールドフラグ                   |
| SPR_OP_FLG    | CHAR(1 BYTE)        | No                         |              | 51        | Operation Flag / 作業フラグ(入荷：棚付、出荷：ピッキング)            |
| SPR_CNFM_FLG  | CHAR(1 BYTE)        | No                         |              | 52        | Confirmed Flag / 確定フラグ                            |
| SPR_RSN_COD   | NVARCHAR2(3 CHAR)   | Yes                        |              | 53        | Reason Code / 理由コード                               |
| SPR_RMKS      | NVARCHAR2(500 CHAR) | Yes                        |              | 54        | Remarks / 備考                                      |
| SPR_RWGT      | NUMBER(12,6)        | No                         | '0'          | 55        | Weight(Result.) / 実績重量                            |
| SPR_RM3       | NUMBER(12,6)        | No                         | '0'          | 56        | Volume(Result.) / 実績容積                            |
| SPR_COI1      | NVARCHAR2(100 CHAR) | Yes                        |              | 57        | Customized Only Item 01 / カスタマイズ項目01              |
| SPR_COI2      | NVARCHAR2(100 CHAR) | Yes                        |              | 58        | Customized Only Item 02 / カスタマイズ項目02              |
| SPR_COI3      | NVARCHAR2(100 CHAR) | Yes                        |              | 59        | Customized Only Item 03 / カスタマイズ項目03              |
| SPR_COI4      | NVARCHAR2(100 CHAR) | Yes                        |              | 60        | Customized Only Item 04 / カスタマイズ項目04              |
| SPR_COI5      | NVARCHAR2(100 CHAR) | Yes                        |              | 61        | Customized Only Item 05 / カスタマイズ項目05              |
| SPR_COI6      | NVARCHAR2(100 CHAR) | Yes                        |              | 62        | Customized Only Item 06 / カスタマイズ項目06              |
| SPR_COI7      | NVARCHAR2(100 CHAR) | Yes                        |              | 63        | Customized Only Item 07 / カスタマイズ項目07              |
| SPR_COI8      | NVARCHAR2(100 CHAR) | Yes                        |              | 64        | Customized Only Item 08 / カスタマイズ項目08              |
| SPR_COI9      | NVARCHAR2(100 CHAR) | Yes                        |              | 65        | Customized Only Item 09 / カスタマイズ項目09              |
| SPR_COI10     | NVARCHAR2(100 CHAR) | Yes                        |              | 66        | Customized Only Item 10 / カスタマイズ項目10              |
| SPR_COI11     | NVARCHAR2(100 CHAR) | Yes                        |              | 67        | Customized Only Item 11 / カスタマイズ項目11              |
| SPR_COI12     | NVARCHAR2(100 CHAR) | Yes                        |              | 68        | Customized Only Item 12 / カスタマイズ項目12              |
| SPR_COI13     | NVARCHAR2(100 CHAR) | Yes                        |              | 69        | Customized Only Item 13 / カスタマイズ項目13              |
| SPR_COI14     | NVARCHAR2(100 CHAR) | Yes                        |              | 70        | Customized Only Item 14 / カスタマイズ項目14              |
| SPR_COI15     | NVARCHAR2(100 CHAR) | Yes                        |              | 71        | Customized Only Item 15 / カスタマイズ項目15              |
| SPR_COI16     | NVARCHAR2(100 CHAR) | Yes                        |              | 72        | Customized Only Item 16 / カスタマイズ項目16              |
| SPR_COI17     | NVARCHAR2(100 CHAR) | Yes                        |              | 73        | Customized Only Item 17 / カスタマイズ項目17              |
| SPR_COI18     | NVARCHAR2(100 CHAR) | Yes                        |              | 74        | Customized Only Item 18 / カスタマイズ項目18              |
| SPR_COI19     | NVARCHAR2(100 CHAR) | Yes                        |              | 75        | Customized Only Item 19 / カスタマイズ項目19              |
| SPR_COI20     | NVARCHAR2(100 CHAR) | Yes                        |              | 76        | Customized Only Item 20 / カスタマイズ項目20              |
| SPR_COI21     | NVARCHAR2(100 CHAR) | Yes                        |              | 77        | Customized Only Item 21 / カスタマイズ項目21              |
| SPR_COI22     | NVARCHAR2(100 CHAR) | Yes                        |              | 78        | Customized Only Item 22 / カスタマイズ項目22              |
| SPR_COI23     | NVARCHAR2(100 CHAR) | Yes                        |              | 79        | Customized Only Item 23 / カスタマイズ項目23              |
| SPR_COI24     | NVARCHAR2(100 CHAR) | Yes                        |              | 80        | Customized Only Item 24 / カスタマイズ項目24              |
| SPR_COI25     | NVARCHAR2(100 CHAR) | Yes                        |              | 81        | Customized Only Item 25 / カスタマイズ項目25              |
| SPR_COI26     | NVARCHAR2(100 CHAR) | Yes                        |              | 82        | Customized Only Item 26 / カスタマイズ項目26              |
| SPR_COI27     | NVARCHAR2(100 CHAR) | Yes                        |              | 83        | Customized Only Item 27 / カスタマイズ項目27              |
| SPR_COI28     | NVARCHAR2(100 CHAR) | Yes                        |              | 84        | Customized Only Item 28 / カスタマイズ項目28              |
| SPR_COI29     | NVARCHAR2(100 CHAR) | Yes                        |              | 85        | Customized Only Item 29 / カスタマイズ項目29              |
| SPR_COI30     | NVARCHAR2(100 CHAR) | Yes                        |              | 86        | Customized Only Item 30 / カスタマイズ項目30              |
| SPR_COI31     | NVARCHAR2(100 CHAR) | Yes                        |              | 87        | Customized Only Item 31 / カスタマイズ項目31              |
| SPR_COI32     | NVARCHAR2(100 CHAR) | Yes                        |              | 88        | Customized Only Item 32 / カスタマイズ項目32              |
| SPR_COI33     | NVARCHAR2(100 CHAR) | Yes                        |              | 89        | Customized Only Item 33 / カスタマイズ項目33              |
| SPR_COI34     | NVARCHAR2(100 CHAR) | Yes                        |              | 90        | Customized Only Item 34 / カスタマイズ項目34              |
| SPR_COI35     | NVARCHAR2(100 CHAR) | Yes                        |              | 91        | Customized Only Item 35 / カスタマイズ項目35              |
| SPR_COI36     | NVARCHAR2(100 CHAR) | Yes                        |              | 92        | Customized Only Item 36 / カスタマイズ項目36              |
| SPR_COI37     | NVARCHAR2(100 CHAR) | Yes                        |              | 93        | Customized Only Item 37 / カスタマイズ項目37              |
| SPR_COI38     | NVARCHAR2(100 CHAR) | Yes                        |              | 94        | Customized Only Item 38 / カスタマイズ項目38              |
| SPR_COI39     | NVARCHAR2(100 CHAR) | Yes                        |              | 95        | Customized Only Item 39 / カスタマイズ項目39              |
| SPR_COI40     | NVARCHAR2(100 CHAR) | Yes                        |              | 96        | Customized Only Item 40 / カスタマイズ項目40              |
| SPR_COIL1     | NVARCHAR2(255 CHAR) | Yes                        |              | 97        | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| SPR_COIL2     | NVARCHAR2(255 CHAR) | Yes                        |              | 98        | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| SPR_COIL3     | NVARCHAR2(255 CHAR) | Yes                        |              | 99        | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| SPR_COIL4     | NVARCHAR2(255 CHAR) | Yes                        |              | 100       | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| SPR_COIL5     | NVARCHAR2(255 CHAR) | Yes                        |              | 101       | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
| SPR_COIL6     | NVARCHAR2(255 CHAR) | Yes                        |              | 102       | Customized Only Item (Long) 06 / カスタマイズ項目(long)06 |
| SPR_COIL7     | NVARCHAR2(255 CHAR) | Yes                        |              | 103       | Customized Only Item (Long) 07 / カスタマイズ項目(long)07 |
| SPR_COIL8     | NVARCHAR2(255 CHAR) | Yes                        |              | 104       | Customized Only Item (Long) 08 / カスタマイズ項目(long)08 |
| SPR_COIL9     | NVARCHAR2(255 CHAR) | Yes                        |              | 105       | Customized Only Item (Long) 09 / カスタマイズ項目(long)09 |
| SPR_COIL10    | NVARCHAR2(255 CHAR) | Yes                        |              | 106       | Customized Only Item (Long) 10 / カスタマイズ項目(long)10 |
| SPR_CNI1_NUM  | NUMBER(9,0)         | Yes                        |              | 107       | Customized Number Item 01 / 顧客専用数字項目01            |
| SPR_CNI2_NUM  | NUMBER(9,0)         | Yes                        |              | 108       | Customized Number Item 02 / 顧客専用数字項目02            |
| SPR_CNI3_NUM  | NUMBER(9,0)         | Yes                        |              | 109       | Customized Number Item 03 / 顧客専用数字項目03            |
| SPR_CNI4_NUM  | NUMBER(9,0)         | Yes                        |              | 110       | Customized Number Item 04 / 顧客専用数字項目04            |
| SPR_CNI5_NUM  | NUMBER(9,0)         | Yes                        |              | 111       | Customized Number Item 05 / 顧客専用数字項目05            |
| SPR_CNI6_NUM  | NUMBER(9,0)         | Yes                        |              | 112       | Customized Number Item 06 / 顧客専用数字項目06            |
| SPR_CNI7_NUM  | NUMBER(9,0)         | Yes                        |              | 113       | Customized Number Item 07 / 顧客専用数字項目07            |
| SPR_CNI8_NUM  | NUMBER(9,0)         | Yes                        |              | 114       | Customized Number Item 08 / 顧客専用数字項目08            |
| SPR_CNI9_NUM  | NUMBER(9,0)         | Yes                        |              | 115       | Customized Number Item 09 / 顧客専用数字項目09            |
| SPR_CNI10_NUM | NUMBER(14,4)        | Yes                        |              | 116       | Customized Number Item 10 / 顧客専用数字項目10            |
| SPR_CNI11_NUM | NUMBER(14,4)        | Yes                        |              | 117       | Customized Number Item 11 / 顧客専用数字項目11            |
| SPR_CNI12_NUM | NUMBER(14,4)        | Yes                        |              | 118       | Customized Number Item 12 / 顧客専用数字項目12            |
| SPR_CNI13_NUM | NUMBER(14,4)        | Yes                        |              | 119       | Customized Number Item 13 / 顧客専用数字項目13            |
| SPR_CNI14_NUM | NUMBER(14,4)        | Yes                        |              | 120       | Customized Number Item 14 / 顧客専用数字項目14            |
| SPR_CNI15_NUM | NUMBER(14,4)        | Yes                        |              | 121       | Customized Number Item 15 / 顧客専用数字項目15            |
| SPR_PPB_QTY   | NUMBER(9,0)         | Yes                        |              | 122       | Number Of Pieces Per Ball / ボール入数                 |
| SPR_RBL_QTY   | NUMBER(9,0)         | No                         | "'0'         |
|    "          | 123                 | Ball QTY(Result.) / ボール実績数 |
