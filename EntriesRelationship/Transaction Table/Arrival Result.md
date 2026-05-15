# GWH_TJ_AV_R
#Entity #Standard #INBOUND 

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
| AVR_CPNY_COD  | NVARCHAR2(6 CHAR)   | No                         |              | 18        | Company Code / カンパニーコード                           |
| AVR_WHS_COD   | NVARCHAR2(4 CHAR)   | No                         |              | 19        | Warehouse Code / 倉庫コード                            |
| AVR_CUST_COD  | NVARCHAR2(13 CHAR)  | No                         |              | 20        | Customer Code / 顧客コード                             |
| AVR_AS_NUM    | NVARCHAR2(6 CHAR)   | No                         |              | 21        | Arrival and Shipping Number / 入出荷番号               |
| AVR_ASLN_NUM  | NUMBER(4,0)         | No                         |              | 22        | Arrival and Shipping Line Number / 入出荷ラインNo       |
| AVR_ASSQ_NUM  | NUMBER(4,0)         | No                         |              | 23        | Arrival and Shipping Seq Number / 入出荷SEQNo        |
| AVR_AS_KND    | NVARCHAR2(2 CHAR)   | No                         |              | 24        | Arrival and Shipping Kind / 入出荷区分                 |
| AVR_TRN_KND   | NVARCHAR2(3 CHAR)   | No                         |              | 25        | Transaction Kind / 伝票区分                           |
| AVR_CRT_YMD   | DATE                | No                         |              | 26        | Create Date / レコード作成日                             |
| AVR_AVSP_YMD  | DATE                | Yes                        |              | 27        | Arrived and Shipped Date / 入出荷日                   |
| AVR_AVSP_STS  | NVARCHAR2(3 CHAR)   | No                         |              | 28        | Arrival and Shipping Status / 入出荷ステータス            |
| AVR_PROD_COD  | NVARCHAR2(50 CHAR)  | No                         |              | 29        | Product Code / 商品コード                              |
| AVR_ORGN_COD  | CHAR(2 BYTE)        | Yes                        |              | 30        | Country Of Origin / 原産国                           |
| AVR_PPCS_QTY  | NUMBER(9,0)         | No                         |              | 31        | Number Of Pieces Per Case / ケース入数                 |
| AVR_RCS_QTY   | NUMBER(9,0)         | No                         |              | 32        | Case QTY(Result.) / ケース実績数                        |
| AVR_RPC_QTY   | NUMBER(12,3)        | No                         |              | 33        | Piece QTY(Result.) / バラ実績数                        |
| AVR_RTPC_QTY  | NUMBER(12,3)        | No                         |              | 34        | Total Piece QTY(Result.) / 総バラ実績数                 |
| AVR_AREA_COD  | CHAR(3 BYTE)        | Yes                        |              | 35        | Area / エリア                                        |
| AVR_RACK_COD  | NVARCHAR2(10 CHAR)  | Yes                        |              | 36        | Rack / ラック                                        |
| AVR_PSTN_COD  | NVARCHAR2(3 CHAR)   | Yes                        |              | 37        | Position / ポジション                                  |
| AVR_LVL_COD   | NVARCHAR2(2 CHAR)   | Yes                        |              | 38        | Level / レベル                                       |
| AVR_SBIV_COD  | NVARCHAR2(20 CHAR)  | No                         |              | 39        | Sub Inventry / 等級コード                              |
| AVR_PIK1      | NVARCHAR2(30 CHAR)  | Yes                        |              | 40        | PICKING KEY1 / ピッキングキー1                           |
| AVR_PIK2      | NVARCHAR2(30 CHAR)  | Yes                        |              | 41        | PICKING KEY2 / ピッキングキー2                           |
| AVR_PIK3      | NVARCHAR2(30 CHAR)  | Yes                        |              | 42        | PICKING KEY3 / ピッキングキー3                           |
| AVR_PIK4      | NVARCHAR2(30 CHAR)  | Yes                        |              | 43        | PICKING KEY4 / ピッキングキー4                           |
| AVR_PIK5      | NVARCHAR2(30 CHAR)  | Yes                        |              | 44        | PICKING KEY5 / ピッキングキー5                           |
| AVR_PIK6      | NVARCHAR2(30 CHAR)  | Yes                        |              | 45        | PICKING KEY6 / ピッキングキー6                           |
| AVR_PIK7      | NVARCHAR2(30 CHAR)  | Yes                        |              | 46        | PICKING KEY7 / ピッキングキー7                           |
| AVR_AV_NUM    | NVARCHAR2(6 CHAR)   | Yes                        |              | 47        | Arrival Number / 入荷番号                             |
| AVR_AVLN_NUM  | NUMBER(4,0)         | Yes                        |              | 48        | Arrival Line No / 入荷ラインNo                         |
| AVR_AVSQ_NUM  | NUMBER(4,0)         | Yes                        |              | 49        | Arrival Seq No / 入荷SEQNo                          |
| AVR_DMG_FLG   | CHAR(1 BYTE)        | No                         |              | 50        | Damage/Hold Flag / ダメージ/ホールドフラグ                   |
| AVR_OP_FLG    | CHAR(1 BYTE)        | No                         |              | 51        | Operation Flag / 作業フラグ(入荷：棚付、出荷：ピッキング)            |
| AVR_CNFM_FLG  | CHAR(1 BYTE)        | No                         |              | 52        | Confirmed Flag / 確定フラグ                            |
| AVR_RSN_COD   | NVARCHAR2(3 CHAR)   | Yes                        |              | 53        | Reason Code / 理由コード                               |
| AVR_RMKS      | NVARCHAR2(500 CHAR) | Yes                        |              | 54        | Remarks / 備考                                      |
| AVR_RWGT      | NUMBER(12,6)        | Yes                        | '0'          | 55        | Weight(Result.) / 実績重量                            |
| AVR_RM3       | NUMBER(12,6)        | Yes                        | '0'          | 56        | Volume(Result.) / 実績容積                            |
| AVR_COI1      | NVARCHAR2(100 CHAR) | Yes                        |              | 57        | Customized Only Item 01 / カスタマイズ項目01              |
| AVR_COI2      | NVARCHAR2(100 CHAR) | Yes                        |              | 58        | Customized Only Item 02 / カスタマイズ項目02              |
| AVR_COI3      | NVARCHAR2(100 CHAR) | Yes                        |              | 59        | Customized Only Item 03 / カスタマイズ項目03              |
| AVR_COI4      | NVARCHAR2(100 CHAR) | Yes                        |              | 60        | Customized Only Item 04 / カスタマイズ項目04              |
| AVR_COI5      | NVARCHAR2(100 CHAR) | Yes                        |              | 61        | Customized Only Item 05 / カスタマイズ項目05              |
| AVR_COI6      | NVARCHAR2(100 CHAR) | Yes                        |              | 62        | Customized Only Item 06 / カスタマイズ項目06              |
| AVR_COI7      | NVARCHAR2(100 CHAR) | Yes                        |              | 63        | Customized Only Item 07 / カスタマイズ項目07              |
| AVR_COI8      | NVARCHAR2(100 CHAR) | Yes                        |              | 64        | Customized Only Item 08 / カスタマイズ項目08              |
| AVR_COI9      | NVARCHAR2(100 CHAR) | Yes                        |              | 65        | Customized Only Item 09 / カスタマイズ項目09              |
| AVR_COI10     | NVARCHAR2(100 CHAR) | Yes                        |              | 66        | Customized Only Item 10 / カスタマイズ項目10              |
| AVR_COI11     | NVARCHAR2(100 CHAR) | Yes                        |              | 67        | Customized Only Item 11 / カスタマイズ項目11              |
| AVR_COI12     | NVARCHAR2(100 CHAR) | Yes                        |              | 68        | Customized Only Item 12 / カスタマイズ項目12              |
| AVR_COI13     | NVARCHAR2(100 CHAR) | Yes                        |              | 69        | Customized Only Item 13 / カスタマイズ項目13              |
| AVR_COI14     | NVARCHAR2(100 CHAR) | Yes                        |              | 70        | Customized Only Item 14 / カスタマイズ項目14              |
| AVR_COI15     | NVARCHAR2(100 CHAR) | Yes                        |              | 71        | Customized Only Item 15 / カスタマイズ項目15              |
| AVR_COI16     | NVARCHAR2(100 CHAR) | Yes                        |              | 72        | Customized Only Item 16 / カスタマイズ項目16              |
| AVR_COI17     | NVARCHAR2(100 CHAR) | Yes                        |              | 73        | Customized Only Item 17 / カスタマイズ項目17              |
| AVR_COI18     | NVARCHAR2(100 CHAR) | Yes                        |              | 74        | Customized Only Item 18 / カスタマイズ項目18              |
| AVR_COI19     | NVARCHAR2(100 CHAR) | Yes                        |              | 75        | Customized Only Item 19 / カスタマイズ項目19              |
| AVR_COI20     | NVARCHAR2(100 CHAR) | Yes                        |              | 76        | Customized Only Item 20 / カスタマイズ項目20              |
| AVR_COI21     | NVARCHAR2(100 CHAR) | Yes                        |              | 77        | Customized Only Item 21 / カスタマイズ項目21              |
| AVR_COI22     | NVARCHAR2(100 CHAR) | Yes                        |              | 78        | Customized Only Item 22 / カスタマイズ項目22              |
| AVR_COI23     | NVARCHAR2(100 CHAR) | Yes                        |              | 79        | Customized Only Item 23 / カスタマイズ項目23              |
| AVR_COI24     | NVARCHAR2(100 CHAR) | Yes                        |              | 80        | Customized Only Item 24 / カスタマイズ項目24              |
| AVR_COI25     | NVARCHAR2(100 CHAR) | Yes                        |              | 81        | Customized Only Item 25 / カスタマイズ項目25              |
| AVR_COI26     | NVARCHAR2(100 CHAR) | Yes                        |              | 82        | Customized Only Item 26 / カスタマイズ項目26              |
| AVR_COI27     | NVARCHAR2(100 CHAR) | Yes                        |              | 83        | Customized Only Item 27 / カスタマイズ項目27              |
| AVR_COI28     | NVARCHAR2(100 CHAR) | Yes                        |              | 84        | Customized Only Item 28 / カスタマイズ項目28              |
| AVR_COI29     | NVARCHAR2(100 CHAR) | Yes                        |              | 85        | Customized Only Item 29 / カスタマイズ項目29              |
| AVR_COI30     | NVARCHAR2(100 CHAR) | Yes                        |              | 86        | Customized Only Item 30 / カスタマイズ項目30              |
| AVR_COI31     | NVARCHAR2(100 CHAR) | Yes                        |              | 87        | Customized Only Item 31 / カスタマイズ項目31              |
| AVR_COI32     | NVARCHAR2(100 CHAR) | Yes                        |              | 88        | Customized Only Item 32 / カスタマイズ項目32              |
| AVR_COI33     | NVARCHAR2(100 CHAR) | Yes                        |              | 89        | Customized Only Item 33 / カスタマイズ項目33              |
| AVR_COI34     | NVARCHAR2(100 CHAR) | Yes                        |              | 90        | Customized Only Item 34 / カスタマイズ項目34              |
| AVR_COI35     | NVARCHAR2(100 CHAR) | Yes                        |              | 91        | Customized Only Item 35 / カスタマイズ項目35              |
| AVR_COI36     | NVARCHAR2(100 CHAR) | Yes                        |              | 92        | Customized Only Item 36 / カスタマイズ項目36              |
| AVR_COI37     | NVARCHAR2(100 CHAR) | Yes                        |              | 93        | Customized Only Item 37 / カスタマイズ項目37              |
| AVR_COI38     | NVARCHAR2(100 CHAR) | Yes                        |              | 94        | Customized Only Item 38 / カスタマイズ項目38              |
| AVR_COI39     | NVARCHAR2(100 CHAR) | Yes                        |              | 95        | Customized Only Item 39 / カスタマイズ項目39              |
| AVR_COI40     | NVARCHAR2(100 CHAR) | Yes                        |              | 96        | Customized Only Item 40 / カスタマイズ項目40              |
| AVR_COIL1     | NVARCHAR2(255 CHAR) | Yes                        |              | 97        | Customized Only Item (Long) 01 / カスタマイズ項目(long)01 |
| AVR_COIL2     | NVARCHAR2(255 CHAR) | Yes                        |              | 98        | Customized Only Item (Long) 02 / カスタマイズ項目(long)02 |
| AVR_COIL3     | NVARCHAR2(255 CHAR) | Yes                        |              | 99        | Customized Only Item (Long) 03 / カスタマイズ項目(long)03 |
| AVR_COIL4     | NVARCHAR2(255 CHAR) | Yes                        |              | 100       | Customized Only Item (Long) 04 / カスタマイズ項目(long)04 |
| AVR_COIL5     | NVARCHAR2(255 CHAR) | Yes                        |              | 101       | Customized Only Item (Long) 05 / カスタマイズ項目(long)05 |
| AVR_COIL6     | NVARCHAR2(255 CHAR) | Yes                        |              | 102       | Customized Only Item (Long) 06 / カスタマイズ項目(long)06 |
| AVR_COIL7     | NVARCHAR2(255 CHAR) | Yes                        |              | 103       | Customized Only Item (Long) 07 / カスタマイズ項目(long)07 |
| AVR_COIL8     | NVARCHAR2(255 CHAR) | Yes                        |              | 104       | Customized Only Item (Long) 08 / カスタマイズ項目(long)08 |
| AVR_COIL9     | NVARCHAR2(255 CHAR) | Yes                        |              | 105       | Customized Only Item (Long) 09 / カスタマイズ項目(long)09 |
| AVR_COIL10    | NVARCHAR2(255 CHAR) | Yes                        |              | 106       | Customized Only Item (Long) 10 / カスタマイズ項目(long)10 |
| AVR_CNI1_NUM  | NUMBER(9,0)         | Yes                        |              | 107       | Customized Number Item 01 / 顧客専用数字項目01            |
| AVR_CNI2_NUM  | NUMBER(9,0)         | Yes                        |              | 108       | Customized Number Item 02 / 顧客専用数字項目02            |
| AVR_CNI3_NUM  | NUMBER(9,0)         | Yes                        |              | 109       | Customized Number Item 03 / 顧客専用数字項目03            |
| AVR_CNI4_NUM  | NUMBER(9,0)         | Yes                        |              | 110       | Customized Number Item 04 / 顧客専用数字項目04            |
| AVR_CNI5_NUM  | NUMBER(9,0)         | Yes                        |              | 111       | Customized Number Item 05 / 顧客専用数字項目05            |
| AVR_CNI6_NUM  | NUMBER(9,0)         | Yes                        |              | 112       | Customized Number Item 06 / 顧客専用数字項目06            |
| AVR_CNI7_NUM  | NUMBER(9,0)         | Yes                        |              | 113       | Customized Number Item 07 / 顧客専用数字項目07            |
| AVR_CNI8_NUM  | NUMBER(9,0)         | Yes                        |              | 114       | Customized Number Item 08 / 顧客専用数字項目08            |
| AVR_CNI9_NUM  | NUMBER(9,0)         | Yes                        |              | 115       | Customized Number Item 09 / 顧客専用数字項目09            |
| AVR_CNI10_NUM | NUMBER(14,4)        | Yes                        |              | 116       | Customized Number Item 10 / 顧客専用数字項目10            |
| AVR_CNI11_NUM | NUMBER(14,4)        | Yes                        |              | 117       | Customized Number Item 11 / 顧客専用数字項目11            |
| AVR_CNI12_NUM | NUMBER(14,4)        | Yes                        |              | 118       | Customized Number Item 12 / 顧客専用数字項目12            |
| AVR_CNI13_NUM | NUMBER(14,4)        | Yes                        |              | 119       | Customized Number Item 13 / 顧客専用数字項目13            |
| AVR_CNI14_NUM | NUMBER(14,4)        | Yes                        |              | 120       | Customized Number Item 14 / 顧客専用数字項目14            |
| AVR_CNI15_NUM | NUMBER(14,4)        | Yes                        |              | 121       | Customized Number Item 15 / 顧客専用数字項目15            |
| AVR_PPB_QTY   | NUMBER(9,0)         | Yes                        |              | 122       | Number Of Pieces Per Ball / ボール入数                 |
| AVR_RBL_QTY   | NUMBER(9,0)         | No                         | "'0'         |
|    "          | 123                 | Ball QTY(Result.) / ボール実績数 |
