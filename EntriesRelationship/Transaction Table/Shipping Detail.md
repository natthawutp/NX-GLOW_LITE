# GWH_TJ_SP_D
#Entity #Standard #OUTBOUND 

| COLUMN_NAME   | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                               |
|---------------|---------------------|----------|--------------|-----------|--------------------------------------------------------|
| DEL_FLG       | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                  |
| CRT_YMD       | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                           |
| CRT_TIM       | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                          |
| CRT_TMID      | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                |
| CRT_USER      | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                               |
| CRT_PGM       | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                          |
| CRT_TM_ZONE   | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                    |
| CRT_YMDHMS    | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）               |
| CRT_L_YMDHMS  | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）            |
| UPD_YMD       | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                           |
| UPD_TIM       | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                          |
| UPD_TMID      | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                            |
| UPD_USER      | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                               |
| UPD_PGM       | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                          |
| UPD_TM_ZONE   | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                    |
| UPD_YMDHMS    | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）               |
| UPD_L_YMDHMS  | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）            |
| SPD_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                |
| SPD_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                 |
| SPD_CUST_COD  | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                  |
| SPD_SP_NUM    | NVARCHAR2(6 CHAR)   | No       |              | 21        | Shipping Number / 出荷番号                                 |
| SPD_SPLN_NUM  | NUMBER(4,0)         | No       |              | 22        | Shipping Line Number / 出荷ラインNo                         |
| SPD_SP_STS    | NVARCHAR2(3 CHAR)   | No       |              | 23        | Shipping Status / 出荷ステータス                              |
| SPD_WHS_RMKS  | NVARCHAR2(500 CHAR) | Yes      |              | 24        | Remarks for Warehouse / 倉庫向け作業指示                       |
| SPD_SOD_RMKS  | NVARCHAR2(500 CHAR) | Yes      |              | 25        | Remarks for Statement of Delivery / 納品書備考              |
| SPD_PROD_COD  | NVARCHAR2(50 CHAR)  | No       |              | 26        | Product Code / 商品コード                                   |
| SPD_PROD_NAM  | NVARCHAR2(100 CHAR) | No       |              | 27        | Product Name / 商品名称                                    |
| SPD_ORGN_COD  | CHAR(2 BYTE)        | Yes      |              | 28        | Country Of Origin / 原産国                                |
| SPD_SPPR_COD  | NVARCHAR2(50 CHAR)  | Yes      |              | 29        | Supplier Product Code / サプライヤ商品コード                     |
| SPD_SPPR_NAM  | NVARCHAR2(100 CHAR) | Yes      |              | 30        | Supplier Product Name / サプライヤ商品名称                      |
| SPD_PPCS_QTY  | NUMBER(9,0)         | No       |              | 31        | Number Of Pieces Per Case / ケース入数                      |
| SPD_SCS_QTY   | NUMBER(9,0)         | No       |              | 32        | Case QTY(Sched.) / ケース予定数                              |
| SPD_SPC_QTY   | NUMBER(12,3)        | No       |              | 33        | Piece QTY(Sched.) / バラ予定数                              |
| SPD_STPC_QTY  | NUMBER(12,3)        | No       |              | 34        | Total Piece QTY(Sched.) / 総バラ予定数                       |
| SPD_SRPC_QTY  | NUMBER(12,3)        | No       |              | 35        | Original Total Piece QTY(Sched.) / 総バラ予定受信数            |
| SPD_PCUM_PCS  | NVARCHAR2(3 CHAR)   | Yes      |              | 36        | UOM(Piece) / 単位(バラ数)                                   |
| SPD_PCUM_CS   | NVARCHAR2(3 CHAR)   | Yes      |              | 37        | UOM(Case) / 単位(ケース数)                                   |
| SPD_WGT       | NUMBER(12,6)        | No       |              | 38        | Weight / 重量                                            |
| SPD_M3        | NUMBER(12,6)        | No       |              | 39        | Volume / 容積                                            |
| SPD_SBIV_COD  | NVARCHAR2(20 CHAR)  | Yes      |              | 40        | Sub Inventry / 等級コード                                   |
| SPD_PIK1      | NVARCHAR2(30 CHAR)  | Yes      |              | 41        | PICKING KEY1 / ピッキングキー1                                |
| SPD_PIK2      | NVARCHAR2(30 CHAR)  | Yes      |              | 42        | PICKING KEY2 / ピッキングキー2                                |
| SPD_PIK3      | NVARCHAR2(30 CHAR)  | Yes      |              | 43        | PICKING KEY3 / ピッキングキー3                                |
| SPD_PIK4      | NVARCHAR2(30 CHAR)  | Yes      |              | 44        | PICKING KEY4 / ピッキングキー4                                |
| SPD_PIK5      | NVARCHAR2(30 CHAR)  | Yes      |              | 45        | PICKING KEY5 / ピッキングキー5                                |
| SPD_PIK6      | NVARCHAR2(30 CHAR)  | Yes      |              | 46        | PICKING KEY6 / ピッキングキー6                                |
| SPD_PIK7      | NVARCHAR2(30 CHAR)  | Yes      |              | 47        | PICKING KEY7 / ピッキングキー7                                |
| SPD_PSSA_FLG  | CHAR(1 BYTE)        | No       |              | 48        | PSSA Kind(Perfect Shaped Stock Allocation) / 成体管理フラグ   |
| SPD_CLI1      | NVARCHAR2(50 CHAR)  | Yes      |              | 49        | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| SPD_CLI2      | NVARCHAR2(50 CHAR)  | Yes      |              | 50        | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| SPD_CLI3      | NVARCHAR2(50 CHAR)  | Yes      |              | 51        | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| SPD_CLI4      | NVARCHAR2(50 CHAR)  | Yes      |              | 52        | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| SPD_CLI5      | NVARCHAR2(50 CHAR)  | Yes      |              | 53        | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| SPD_CLI6      | NVARCHAR2(50 CHAR)  | Yes      |              | 54        | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| SPD_CLI7      | NVARCHAR2(50 CHAR)  | Yes      |              | 55        | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| SPD_CLI8      | NVARCHAR2(50 CHAR)  | Yes      |              | 56        | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| SPD_CLI9      | NVARCHAR2(50 CHAR)  | Yes      |              | 57        | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| SPD_CDSQ_NUM  | NVARCHAR2(10 CHAR)  | Yes      |              | 58        | Customer Detail SEQ / 顧客明細SEQ                          |
| SPD_NOSP_FLG  | CHAR(1 BYTE)        | No       |              | 59        | Not Shipping Flag / 出荷対象外フラグ                           |
| SPD_ALCT_KBN  | CHAR(1 BYTE)        | Yes      |              | 60        | Allocation Method Kind / 引当方法区分                        |
| SPD_DMG_FLG   | CHAR(1 BYTE)        | No       |              | 61        | Damage/Hold Flag / ダメージ/ホールドフラグ                        |
| SPD_CLI10     | NVARCHAR2(50 CHAR)  | Yes      |              | 62        | Customized List Items for printing 10 / 顧客専用リスト印字用項目10 |
| SPD_CLI11     | NVARCHAR2(50 CHAR)  | Yes      |              | 63        | Customized List Items for printing 11 / 顧客専用リスト印字用項目11 |
| SPD_CLI12     | NVARCHAR2(50 CHAR)  | Yes      |              | 64        | Customized List Items for printing 12 / 顧客専用リスト印字用項目12 |
| SPD_CLI13     | NVARCHAR2(50 CHAR)  | Yes      |              | 65        | Customized List Items for printing 13 / 顧客専用リスト印字用項目13 |
| SPD_CLI14     | NVARCHAR2(50 CHAR)  | Yes      |              | 66        | Customized List Items for printing 14 / 顧客専用リスト印字用項目14 |
| SPD_CLI15     | NVARCHAR2(50 CHAR)  | Yes      |              | 67        | Customized List Items for printing 15 / 顧客専用リスト印字用項目15 |
| SPD_CFG1_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 68        | Customized Flag 01 / 顧客専用フラグ01                         |
| SPD_CFG2_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 69        | Customized Flag 02 / 顧客専用フラグ02                         |
| SPD_CFG3_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 70        | Customized Flag 03 / 顧客専用フラグ03                         |
| SPD_CFG4_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 71        | Customized Flag 04 / 顧客専用フラグ04                         |
| SPD_CFG5_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 72        | Customized Flag 05 / 顧客専用フラグ05                         |
| SPD_CFG6_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 73        | Customized Flag 06 / 顧客専用フラグ06                         |
| SPD_CFG7_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 74        | Customized Flag 07 / 顧客専用フラグ07                         |
| SPD_CFG8_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 75        | Customized Flag 08 / 顧客専用フラグ08                         |
| SPD_CFG9_FLG  | CHAR(1 BYTE)        | No       | 'N'          | 76        | Customized Flag 09 / 顧客専用フラグ09                         |
| SPD_CFG10_FLG | CHAR(1 BYTE)        | No       | 'N'          | 77        | Customized Flag 10 / 顧客専用フラグ10                         |
| SPD_CFG11_FLG | CHAR(1 BYTE)        | No       | 'N'          | 78        | Customized Flag 11 / 顧客専用フラグ11                         |
| SPD_CFG12_FLG | CHAR(1 BYTE)        | No       | 'N'          | 79        | Customized Flag 12 / 顧客専用フラグ12                         |
| SPD_CFG13_FLG | CHAR(1 BYTE)        | No       | 'N'          | 80        | Customized Flag 13 / 顧客専用フラグ13                         |
| SPD_CFG14_FLG | CHAR(1 BYTE)        | No       | 'N'          | 81        | Customized Flag 14 / 顧客専用フラグ14                         |
| SPD_CFG15_FLG | CHAR(1 BYTE)        | No       | 'N'          | 82        | Customized Flag 15 / 顧客専用フラグ15                         |
| SPD_CNI1_NUM  | NUMBER(9,0)         | Yes      |              | 83        | Customized Number Item 01 / 顧客専用数字項目01                 |
| SPD_CNI2_NUM  | NUMBER(9,0)         | Yes      |              | 84        | Customized Number Item 02 / 顧客専用数字項目02                 |
| SPD_CNI3_NUM  | NUMBER(9,0)         | Yes      |              | 85        | Customized Number Item 03 / 顧客専用数字項目03                 |
| SPD_CNI4_NUM  | NUMBER(9,0)         | Yes      |              | 86        | Customized Number Item 04 / 顧客専用数字項目04                 |
| SPD_CNI5_NUM  | NUMBER(9,0)         | Yes      |              | 87        | Customized Number Item 05 / 顧客専用数字項目05                 |
| SPD_CNI6_NUM  | NUMBER(9,0)         | Yes      |              | 88        | Customized Number Item 06 / 顧客専用数字項目06                 |
| SPD_CNI7_NUM  | NUMBER(9,0)         | Yes      |              | 89        | Customized Number Item 07 / 顧客専用数字項目07                 |
| SPD_CNI8_NUM  | NUMBER(9,0)         | Yes      |              | 90        | Customized Number Item 08 / 顧客専用数字項目08                 |
| SPD_CNI9_NUM  | NUMBER(9,0)         | Yes      |              | 91        | Customized Number Item 09 / 顧客専用数字項目09                 |
| SPD_CNI10_NUM | NUMBER(14,4)        | Yes      |              | 92        | Customized Number Item 10 / 顧客専用数字項目10                 |
| SPD_CNI11_NUM | NUMBER(14,4)        | Yes      |              | 93        | Customized Number Item 11 / 顧客専用数字項目11                 |
| SPD_CNI12_NUM | NUMBER(14,4)        | Yes      |              | 94        | Customized Number Item 12 / 顧客専用数字項目12                 |
| SPD_CNI13_NUM | NUMBER(14,4)        | Yes      |              | 95        | Customized Number Item 13 / 顧客専用数字項目13                 |
| SPD_CNI14_NUM | NUMBER(14,4)        | Yes      |              | 96        | Customized Number Item 14 / 顧客専用数字項目14                 |
| SPD_CNI15_NUM | NUMBER(14,4)        | Yes      |              | 97        | Customized Number Item 15 / 顧客専用数字項目15                 |
| SPD_COI1      | NVARCHAR2(100 CHAR) | Yes      |              | 98        | Customized Only Item 01 / カスタマイズ項目01                   |
| SPD_COI2      | NVARCHAR2(100 CHAR) | Yes      |              | 99        | Customized Only Item 02 / カスタマイズ項目02                   |
| SPD_COI3      | NVARCHAR2(100 CHAR) | Yes      |              | 100       | Customized Only Item 03 / カスタマイズ項目03                   |
| SPD_COI4      | NVARCHAR2(100 CHAR) | Yes      |              | 101       | Customized Only Item 04 / カスタマイズ項目04                   |
| SPD_COI5      | NVARCHAR2(100 CHAR) | Yes      |              | 102       | Customized Only Item 05 / カスタマイズ項目05                   |
| SPD_COI6      | NVARCHAR2(100 CHAR) | Yes      |              | 103       | Customized Only Item 06 / カスタマイズ項目06                   |
| SPD_COI7      | NVARCHAR2(100 CHAR) | Yes      |              | 104       | Customized Only Item 07 / カスタマイズ項目07                   |
| SPD_COI8      | NVARCHAR2(100 CHAR) | Yes      |              | 105       | Customized Only Item 08 / カスタマイズ項目08                   |
| SPD_COI9      | NVARCHAR2(100 CHAR) | Yes      |              | 106       | Customized Only Item 09 / カスタマイズ項目09                   |
| SPD_COI10     | NVARCHAR2(100 CHAR) | Yes      |              | 107       | Customized Only Item 10 / カスタマイズ項目10                   |
| SPD_COI11     | NVARCHAR2(100 CHAR) | Yes      |              | 108       | Customized Only Item 11 / カスタマイズ項目11                   |
| SPD_COI12     | NVARCHAR2(100 CHAR) | Yes      |              | 109       | Customized Only Item 12 / カスタマイズ項目12                   |
| SPD_COI13     | NVARCHAR2(100 CHAR) | Yes      |              | 110       | Customized Only Item 13 / カスタマイズ項目13                   |
| SPD_COI14     | NVARCHAR2(100 CHAR) | Yes      |              | 111       | Customized Only Item 14 / カスタマイズ項目14                   |
| SPD_COI15     | NVARCHAR2(100 CHAR) | Yes      |              | 112       | Customized Only Item 15 / カスタマイズ項目15                   |
| SPD_COI16     | NVARCHAR2(100 CHAR) | Yes      |              | 113       | Customized Only Item 16 / カスタマイズ項目16                   |
| SPD_COI17     | NVARCHAR2(100 CHAR) | Yes      |              | 114       | Customized Only Item 17 / カスタマイズ項目17                   |
| SPD_COI18     | NVARCHAR2(100 CHAR) | Yes      |              | 115       | Customized Only Item 18 / カスタマイズ項目18                   |
| SPD_COI19     | NVARCHAR2(100 CHAR) | Yes      |              | 116       | Customized Only Item 19 / カスタマイズ項目19                   |
| SPD_COI20     | NVARCHAR2(100 CHAR) | Yes      |              | 117       | Customized Only Item 20 / カスタマイズ項目20                   |
| SPD_COI21     | NVARCHAR2(100 CHAR) | Yes      |              | 118       | Customized Only Item 21 / カスタマイズ項目21                   |
| SPD_COI22     | NVARCHAR2(100 CHAR) | Yes      |              | 119       | Customized Only Item 22 / カスタマイズ項目22                   |
| SPD_COI23     | NVARCHAR2(100 CHAR) | Yes      |              | 120       | Customized Only Item 23 / カスタマイズ項目23                   |
| SPD_COI24     | NVARCHAR2(100 CHAR) | Yes      |              | 121       | Customized Only Item 24 / カスタマイズ項目24                   |
| SPD_COI25     | NVARCHAR2(100 CHAR) | Yes      |              | 122       | Customized Only Item 25 / カスタマイズ項目25                   |
| SPD_COI26     | NVARCHAR2(100 CHAR) | Yes      |              | 123       | Customized Only Item 26 / カスタマイズ項目26                   |
| SPD_COI27     | NVARCHAR2(100 CHAR) | Yes      |              | 124       | Customized Only Item 27 / カスタマイズ項目27                   |
| SPD_COI28     | NVARCHAR2(100 CHAR) | Yes      |              | 125       | Customized Only Item 28 / カスタマイズ項目28                   |
| SPD_COI29     | NVARCHAR2(100 CHAR) | Yes      |              | 126       | Customized Only Item 29 / カスタマイズ項目29                   |
| SPD_COI30     | NVARCHAR2(100 CHAR) | Yes      |              | 127       | Customized Only Item 30 / カスタマイズ項目30                   |
| SPD_COI31     | NVARCHAR2(100 CHAR) | Yes      |              | 128       | Customized Only Item 31 / カスタマイズ項目31                   |
| SPD_COI32     | NVARCHAR2(100 CHAR) | Yes      |              | 129       | Customized Only Item 32 / カスタマイズ項目32                   |
| SPD_COI33     | NVARCHAR2(100 CHAR) | Yes      |              | 130       | Customized Only Item 33 / カスタマイズ項目33                   |
| SPD_COI34     | NVARCHAR2(100 CHAR) | Yes      |              | 131       | Customized Only Item 34 / カスタマイズ項目34                   |
| SPD_COI35     | NVARCHAR2(100 CHAR) | Yes      |              | 132       | Customized Only Item 35 / カスタマイズ項目35                   |
| SPD_COI36     | NVARCHAR2(100 CHAR) | Yes      |              | 133       | Customized Only Item 36 / カスタマイズ項目36                   |
| SPD_COI37     | NVARCHAR2(100 CHAR) | Yes      |              | 134       | Customized Only Item 37 / カスタマイズ項目37                   |
| SPD_COI38     | NVARCHAR2(100 CHAR) | Yes      |              | 135       | Customized Only Item 38 / カスタマイズ項目38                   |
| SPD_COI39     | NVARCHAR2(100 CHAR) | Yes      |              | 136       | Customized Only Item 39 / カスタマイズ項目39                   |
| SPD_COI40     | NVARCHAR2(100 CHAR) | Yes      |              | 137       | Customized Only Item 40 / カスタマイズ項目40                   |
| SPD_COIL1     | NVARCHAR2(255 CHAR) | Yes      |              | 138       | Customized Only Item (Long) 01 / カスタマイズ項目(long)01      |
| SPD_COIL2     | NVARCHAR2(255 CHAR) | Yes      |              | 139       | Customized Only Item (Long) 02 / カスタマイズ項目(long)02      |
| SPD_COIL3     | NVARCHAR2(255 CHAR) | Yes      |              | 140       | Customized Only Item (Long) 03 / カスタマイズ項目(long)03      |
| SPD_COIL4     | NVARCHAR2(255 CHAR) | Yes      |              | 141       | Customized Only Item (Long) 04 / カスタマイズ項目(long)04      |
| SPD_COIL5     | NVARCHAR2(255 CHAR) | Yes      |              | 142       | Customized Only Item (Long) 05 / カスタマイズ項目(long)05      |
| SPD_COIL6     | NVARCHAR2(255 CHAR) | Yes      |              | 143       | Customized Only Item (Long) 06 / カスタマイズ項目(long)06      |
| SPD_COIL7     | NVARCHAR2(255 CHAR) | Yes      |              | 144       | Customized Only Item (Long) 07 / カスタマイズ項目(long)07      |
| SPD_COIL8     | NVARCHAR2(255 CHAR) | Yes      |              | 145       | Customized Only Item (Long) 08 / カスタマイズ項目(long)08      |
| SPD_COIL9     | NVARCHAR2(255 CHAR) | Yes      |              | 146       | Customized Only Item (Long) 09 / カスタマイズ項目(long)09      |
| SPD_COIL10    | NVARCHAR2(255 CHAR) | Yes      |              | 147       | Customized Only Item (Long) 10 / カスタマイズ項目(long)10      |
| SPD_PPB_QTY   | NUMBER(9,0)         | Yes      |              | 148       | Number Of Pieces Per Ball / ボール入数                      |
| SPD_SBL_QTY   | NUMBER(9,0)         | No       | '0'          | 149       | Ball QTY(Sched.) / ボール予定数                              |
| SPD_PCUM_BL   | NVARCHAR2(3 CHAR)   | Yes      |              | 150       | UOM (Ball)                                             |
