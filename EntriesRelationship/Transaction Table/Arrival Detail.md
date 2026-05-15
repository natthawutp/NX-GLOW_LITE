# GWH_TJ_AV_D
#Entity #Standard #INBOUND 

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
| AVD_CPNY_COD  | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                |
| AVD_WHS_COD   | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                 |
| AVD_CUST_COD  | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                  |
| AVD_AV_NUM    | NVARCHAR2(6 CHAR)   | No       |              | 21        | Arrival Number / 入荷番号                                  |
| AVD_AVLN_NUM  | NUMBER(4,0)         | No       |              | 22        | Arrival Line No / 入荷ラインNo                              |
| AVD_AV_STS    | NVARCHAR2(3 CHAR)   | No       |              | 23        | Arrival Status / 入荷ステータス                               |
| AVD_INSP_STS  | CHAR(1 BYTE)        | No       |              | 24        | Arrival Inspected Flag / 入荷検品済フラグ                      |
| AVD_PROD_COD  | NVARCHAR2(50 CHAR)  | No       |              | 25        | Product Code / 商品コード                                   |
| AVD_PROD_NAM  | NVARCHAR2(100 CHAR) | Yes      |              | 26        | Product Name / 商品名称                                    |
| AVD_ORGN_COD  | CHAR(2 BYTE)        | Yes      |              | 27        | Country Of Origin / 原産国                                |
| AVD_SPPR_COD  | NVARCHAR2(50 CHAR)  | Yes      |              | 28        | Supplier Product Code / サプライヤ商品コード                     |
| AVD_SPPR_NAM  | NVARCHAR2(100 CHAR) | Yes      |              | 29        | Supplier Product Name / サプライヤ商品名称                      |
| AVD_PPCS_QTY  | NUMBER(9,0)         | No       |              | 30        | Number Of Pieces Per Case / ケース入数                      |
| AVD_SCS_QTY   | NUMBER(9,0)         | No       |              | 31        | Case QTY(Sched.) / ケース予定数                              |
| AVD_SPC_QTY   | NUMBER(12,3)        | No       |              | 32        | Piece QTY(Sched.) / バラ予定数                              |
| AVD_STPC_QTY  | NUMBER(12,3)        | No       |              | 33        | Total Piece QTY(Sched.) / 総バラ予定数                       |
| AVD_SRPC_QTY  | NUMBER(12,3)        | No       |              | 34        | Original Total Piece QTY(Sched.) / 総バラ予定受信数            |
| AVD_PCUM_PCS  | NVARCHAR2(3 CHAR)   | Yes      |              | 35        | UOM(Piece) / 単位(バラ数)                                   |
| AVD_PCUM_CS   | NVARCHAR2(3 CHAR)   | Yes      |              | 36        | UOM(Case) / 単位(ケース数)                                   |
| AVD_WGT       | NUMBER(12,6)        | Yes      |              | 37        | Weight / 重量                                            |
| AVD_M3        | NUMBER(12,6)        | Yes      |              | 38        | Volume / 容積                                            |
| AVD_SBIV_COD  | NVARCHAR2(20 CHAR)  | No       |              | 39        | Sub Inventry / 等級コード                                   |
| AVD_PIK1      | NVARCHAR2(30 CHAR)  | Yes      |              | 40        | PICKING KEY1 / ピッキングキー1                                |
| AVD_PIK2      | NVARCHAR2(30 CHAR)  | Yes      |              | 41        | PICKING KEY2 / ピッキングキー2                                |
| AVD_PIK3      | NVARCHAR2(30 CHAR)  | Yes      |              | 42        | PICKING KEY3 / ピッキングキー3                                |
| AVD_PIK4      | NVARCHAR2(30 CHAR)  | Yes      |              | 43        | PICKING KEY4 / ピッキングキー4                                |
| AVD_PIK5      | NVARCHAR2(30 CHAR)  | Yes      |              | 44        | PICKING KEY5 / ピッキングキー5                                |
| AVD_PIK6      | NVARCHAR2(30 CHAR)  | Yes      |              | 45        | PICKING KEY6 / ピッキングキー6                                |
| AVD_PIK7      | NVARCHAR2(30 CHAR)  | Yes      |              | 46        | PICKING KEY7 / ピッキングキー7                                |
| AVD_PSSA_FLG  | CHAR(1 BYTE)        | No       |              | 47        | PSSA Kind(Perfect Shaped Stock Allocation) / 成体管理フラグ   |
| AVD_CLPR_FLG  | CHAR(1 BYTE)        | No       |              | 48        | Cargo ID Printed Flag / カーゴIDラベル発行フラグ                  |
| AVD_CDSQ_NUM  | NVARCHAR2(10 CHAR)  | Yes      |              | 49        | Customer Detail SEQ / 顧客明細SEQ                          |
| AVD_DMG_FLG   | CHAR(1 BYTE)        | No       |              | 50        | Damage/Hold Flag / ダメージ/ホールドフラグ                        |
| AVD_CGID_NUM  | NUMBER(4,0)         | Yes      |              | 51        | Number Of Printed Cargo ID Label / カーゴIDラベル発行枚数        |
| AVD_RMKS      | NVARCHAR2(500 CHAR) | Yes      |              | 52        | Remarks for Line / 明細備考                                |
| AVD_CLI1      | NVARCHAR2(50 CHAR)  | Yes      |              | 53        | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| AVD_CLI2      | NVARCHAR2(50 CHAR)  | Yes      |              | 54        | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| AVD_CLI3      | NVARCHAR2(50 CHAR)  | Yes      |              | 55        | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| AVD_CLI4      | NVARCHAR2(50 CHAR)  | Yes      |              | 56        | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| AVD_CLI5      | NVARCHAR2(50 CHAR)  | Yes      |              | 57        | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| AVD_CLI6      | NVARCHAR2(50 CHAR)  | Yes      |              | 58        | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| AVD_CLI7      | NVARCHAR2(50 CHAR)  | Yes      |              | 59        | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| AVD_CLI8      | NVARCHAR2(50 CHAR)  | Yes      |              | 60        | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| AVD_CLI9      | NVARCHAR2(50 CHAR)  | Yes      |              | 61        | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| AVD_CLI10     | NVARCHAR2(50 CHAR)  | Yes      |              | 62        | Customized List Items for printing 10 / 顧客専用リスト印字用項目10 |
| AVD_CLI11     | NVARCHAR2(50 CHAR)  | Yes      |              | 63        | Customized List Items for printing 11 / 顧客専用リスト印字用項目11 |
| AVD_CLI12     | NVARCHAR2(50 CHAR)  | Yes      |              | 64        | Customized List Items for printing 12 / 顧客専用リスト印字用項目12 |
| AVD_CLI13     | NVARCHAR2(50 CHAR)  | Yes      |              | 65        | Customized List Items for printing 13 / 顧客専用リスト印字用項目13 |
| AVD_CLI14     | NVARCHAR2(50 CHAR)  | Yes      |              | 66        | Customized List Items for printing 14 / 顧客専用リスト印字用項目14 |
| AVD_CLI15     | NVARCHAR2(50 CHAR)  | Yes      |              | 67        | Customized List Items for printing 15 / 顧客専用リスト印字用項目15 |
| AVD_CFG1_FLG  | CHAR(1 BYTE)        | No       | N            | 68        | Customized Flag 01 / 顧客専用フラグ01                         |
| AVD_CFG2_FLG  | CHAR(1 BYTE)        | No       | N            | 69        | Customized Flag 02 / 顧客専用フラグ02                         |
| AVD_CFG3_FLG  | CHAR(1 BYTE)        | No       | N            | 70        | Customized Flag 03 / 顧客専用フラグ03                         |
| AVD_CFG4_FLG  | CHAR(1 BYTE)        | No       | N            | 71        | Customized Flag 04 / 顧客専用フラグ04                         |
| AVD_CFG5_FLG  | CHAR(1 BYTE)        | No       | N            | 72        | Customized Flag 05 / 顧客専用フラグ05                         |
| AVD_CFG6_FLG  | CHAR(1 BYTE)        | No       | N            | 73        | Customized Flag 06 / 顧客専用フラグ06                         |
| AVD_CFG7_FLG  | CHAR(1 BYTE)        | No       | N            | 74        | Customized Flag 07 / 顧客専用フラグ07                         |
| AVD_CFG8_FLG  | CHAR(1 BYTE)        | No       | N            | 75        | Customized Flag 08 / 顧客専用フラグ08                         |
| AVD_CFG9_FLG  | CHAR(1 BYTE)        | No       | N            | 76        | Customized Flag 09 / 顧客専用フラグ09                         |
| AVD_CFG10_FLG | CHAR(1 BYTE)        | No       | N            | 77        | Customized Flag 10 / 顧客専用フラグ10                         |
| AVD_CFG11_FLG | CHAR(1 BYTE)        | No       | N            | 78        | Customized Flag 11 / 顧客専用フラグ11                         |
| AVD_CFG12_FLG | CHAR(1 BYTE)        | No       | N            | 79        | Customized Flag 12 / 顧客専用フラグ12                         |
| AVD_CFG13_FLG | CHAR(1 BYTE)        | No       | N            | 80        | Customized Flag 13 / 顧客専用フラグ13                         |
| AVD_CFG14_FLG | CHAR(1 BYTE)        | No       | N            | 81        | Customized Flag 14 / 顧客専用フラグ14                         |
| AVD_CFG15_FLG | CHAR(1 BYTE)        | No       | N            | 82        | Customized Flag 15 / 顧客専用フラグ15                         |
| AVD_CNI1_NUM  | NUMBER(9,0)         | Yes      |              | 83        | Customized Number Item 01 / 顧客専用数字項目01                 |
| AVD_CNI2_NUM  | NUMBER(9,0)         | Yes      |              | 84        | Customized Number Item 02 / 顧客専用数字項目02                 |
| AVD_CNI3_NUM  | NUMBER(9,0)         | Yes      |              | 85        | Customized Number Item 03 / 顧客専用数字項目03                 |
| AVD_CNI4_NUM  | NUMBER(9,0)         | Yes      |              | 86        | Customized Number Item 04 / 顧客専用数字項目04                 |
| AVD_CNI5_NUM  | NUMBER(9,0)         | Yes      |              | 87        | Customized Number Item 05 / 顧客専用数字項目05                 |
| AVD_CNI6_NUM  | NUMBER(9,0)         | Yes      |              | 88        | Customized Number Item 06 / 顧客専用数字項目06                 |
| AVD_CNI7_NUM  | NUMBER(9,0)         | Yes      |              | 89        | Customized Number Item 07 / 顧客専用数字項目07                 |
| AVD_CNI8_NUM  | NUMBER(9,0)         | Yes      |              | 90        | Customized Number Item 08 / 顧客専用数字項目08                 |
| AVD_CNI9_NUM  | NUMBER(9,0)         | Yes      |              | 91        | Customized Number Item 09 / 顧客専用数字項目09                 |
| AVD_CNI10_NUM | NUMBER(14,4)        | Yes      |              | 92        | Customized Number Item 10 / 顧客専用数字項目10                 |
| AVD_CNI11_NUM | NUMBER(14,4)        | Yes      |              | 93        | Customized Number Item 11 / 顧客専用数字項目11                 |
| AVD_CNI12_NUM | NUMBER(14,4)        | Yes      |              | 94        | Customized Number Item 12 / 顧客専用数字項目12                 |
| AVD_CNI13_NUM | NUMBER(14,4)        | Yes      |              | 95        | Customized Number Item 13 / 顧客専用数字項目13                 |
| AVD_CNI14_NUM | NUMBER(14,4)        | Yes      |              | 96        | Customized Number Item 14 / 顧客専用数字項目14                 |
| AVD_CNI15_NUM | NUMBER(14,4)        | Yes      |              | 97        | Customized Number Item 15 / 顧客専用数字項目15                 |
| AVD_PLPR_FLG  | CHAR(1 BYTE)        | No       | N            | 98        | Product Label Printed Flag / 商品ラベル発行フラグ                |
| AVD_ISPR_FLG  | CHAR(1 BYTE)        | No       | N            | 99        | Arrival Pallet Sheet Printed Flag / 入荷シート発行フラグ         |
| AVD_COI1      | NVARCHAR2(100 CHAR) | Yes      |              | 100       | Customized Only Item 01 / カスタマイズ項目01                   |
| AVD_COI2      | NVARCHAR2(100 CHAR) | Yes      |              | 101       | Customized Only Item 02 / カスタマイズ項目02                   |
| AVD_COI3      | NVARCHAR2(100 CHAR) | Yes      |              | 102       | Customized Only Item 03 / カスタマイズ項目03                   |
| AVD_COI4      | NVARCHAR2(100 CHAR) | Yes      |              | 103       | Customized Only Item 04 / カスタマイズ項目04                   |
| AVD_COI5      | NVARCHAR2(100 CHAR) | Yes      |              | 104       | Customized Only Item 05 / カスタマイズ項目05                   |
| AVD_COI6      | NVARCHAR2(100 CHAR) | Yes      |              | 105       | Customized Only Item 06 / カスタマイズ項目06                   |
| AVD_COI7      | NVARCHAR2(100 CHAR) | Yes      |              | 106       | Customized Only Item 07 / カスタマイズ項目07                   |
| AVD_COI8      | NVARCHAR2(100 CHAR) | Yes      |              | 107       | Customized Only Item 08 / カスタマイズ項目08                   |
| AVD_COI9      | NVARCHAR2(100 CHAR) | Yes      |              | 108       | Customized Only Item 09 / カスタマイズ項目09                   |
| AVD_COI10     | NVARCHAR2(100 CHAR) | Yes      |              | 109       | Customized Only Item 10 / カスタマイズ項目10                   |
| AVD_COI11     | NVARCHAR2(100 CHAR) | Yes      |              | 110       | Customized Only Item 11 / カスタマイズ項目11                   |
| AVD_COI12     | NVARCHAR2(100 CHAR) | Yes      |              | 111       | Customized Only Item 12 / カスタマイズ項目12                   |
| AVD_COI13     | NVARCHAR2(100 CHAR) | Yes      |              | 112       | Customized Only Item 13 / カスタマイズ項目13                   |
| AVD_COI14     | NVARCHAR2(100 CHAR) | Yes      |              | 113       | Customized Only Item 14 / カスタマイズ項目14                   |
| AVD_COI15     | NVARCHAR2(100 CHAR) | Yes      |              | 114       | Customized Only Item 15 / カスタマイズ項目15                   |
| AVD_COI16     | NVARCHAR2(100 CHAR) | Yes      |              | 115       | Customized Only Item 16 / カスタマイズ項目16                   |
| AVD_COI17     | NVARCHAR2(100 CHAR) | Yes      |              | 116       | Customized Only Item 17 / カスタマイズ項目17                   |
| AVD_COI18     | NVARCHAR2(100 CHAR) | Yes      |              | 117       | Customized Only Item 18 / カスタマイズ項目18                   |
| AVD_COI19     | NVARCHAR2(100 CHAR) | Yes      |              | 118       | Customized Only Item 19 / カスタマイズ項目19                   |
| AVD_COI20     | NVARCHAR2(100 CHAR) | Yes      |              | 119       | Customized Only Item 20 / カスタマイズ項目20                   |
| AVD_COI21     | NVARCHAR2(100 CHAR) | Yes      |              | 120       | Customized Only Item 21 / カスタマイズ項目21                   |
| AVD_COI22     | NVARCHAR2(100 CHAR) | Yes      |              | 121       | Customized Only Item 22 / カスタマイズ項目22                   |
| AVD_COI23     | NVARCHAR2(100 CHAR) | Yes      |              | 122       | Customized Only Item 23 / カスタマイズ項目23                   |
| AVD_COI24     | NVARCHAR2(100 CHAR) | Yes      |              | 123       | Customized Only Item 24 / カスタマイズ項目24                   |
| AVD_COI25     | NVARCHAR2(100 CHAR) | Yes      |              | 124       | Customized Only Item 25 / カスタマイズ項目25                   |
| AVD_COI26     | NVARCHAR2(100 CHAR) | Yes      |              | 125       | Customized Only Item 26 / カスタマイズ項目26                   |
| AVD_COI27     | NVARCHAR2(100 CHAR) | Yes      |              | 126       | Customized Only Item 27 / カスタマイズ項目27                   |
| AVD_COI28     | NVARCHAR2(100 CHAR) | Yes      |              | 127       | Customized Only Item 28 / カスタマイズ項目28                   |
| AVD_COI29     | NVARCHAR2(100 CHAR) | Yes      |              | 128       | Customized Only Item 29 / カスタマイズ項目29                   |
| AVD_COI30     | NVARCHAR2(100 CHAR) | Yes      |              | 129       | Customized Only Item 30 / カスタマイズ項目30                   |
| AVD_COI31     | NVARCHAR2(100 CHAR) | Yes      |              | 130       | Customized Only Item 31 / カスタマイズ項目31                   |
| AVD_COI32     | NVARCHAR2(100 CHAR) | Yes      |              | 131       | Customized Only Item 32 / カスタマイズ項目32                   |
| AVD_COI33     | NVARCHAR2(100 CHAR) | Yes      |              | 132       | Customized Only Item 33 / カスタマイズ項目33                   |
| AVD_COI34     | NVARCHAR2(100 CHAR) | Yes      |              | 133       | Customized Only Item 34 / カスタマイズ項目34                   |
| AVD_COI35     | NVARCHAR2(100 CHAR) | Yes      |              | 134       | Customized Only Item 35 / カスタマイズ項目35                   |
| AVD_COI36     | NVARCHAR2(100 CHAR) | Yes      |              | 135       | Customized Only Item 36 / カスタマイズ項目36                   |
| AVD_COI37     | NVARCHAR2(100 CHAR) | Yes      |              | 136       | Customized Only Item 37 / カスタマイズ項目37                   |
| AVD_COI38     | NVARCHAR2(100 CHAR) | Yes      |              | 137       | Customized Only Item 38 / カスタマイズ項目38                   |
| AVD_COI39     | NVARCHAR2(100 CHAR) | Yes      |              | 138       | Customized Only Item 39 / カスタマイズ項目39                   |
| AVD_COI40     | NVARCHAR2(100 CHAR) | Yes      |              | 139       | Customized Only Item 40 / カスタマイズ項目40                   |
| AVD_COIL1     | NVARCHAR2(255 CHAR) | Yes      |              | 140       | Customized Only Item (Long) 01 / カスタマイズ項目(long)01      |
| AVD_COIL2     | NVARCHAR2(255 CHAR) | Yes      |              | 141       | Customized Only Item (Long) 02 / カスタマイズ項目(long)02      |
| AVD_COIL3     | NVARCHAR2(255 CHAR) | Yes      |              | 142       | Customized Only Item (Long) 03 / カスタマイズ項目(long)03      |
| AVD_COIL4     | NVARCHAR2(255 CHAR) | Yes      |              | 143       | Customized Only Item (Long) 04 / カスタマイズ項目(long)04      |
| AVD_COIL5     | NVARCHAR2(255 CHAR) | Yes      |              | 144       | Customized Only Item (Long) 05 / カスタマイズ項目(long)05      |
| AVD_COIL6     | NVARCHAR2(255 CHAR) | Yes      |              | 145       | Customized Only Item (Long) 06 / カスタマイズ項目(long)06      |
| AVD_COIL7     | NVARCHAR2(255 CHAR) | Yes      |              | 146       | Customized Only Item (Long) 07 / カスタマイズ項目(long)07      |
| AVD_COIL8     | NVARCHAR2(255 CHAR) | Yes      |              | 147       | Customized Only Item (Long) 08 / カスタマイズ項目(long)08      |
| AVD_COIL9     | NVARCHAR2(255 CHAR) | Yes      |              | 148       | Customized Only Item (Long) 09 / カスタマイズ項目(long)09      |
| AVD_COIL10    | NVARCHAR2(255 CHAR) | Yes      |              | 149       | Customized Only Item (Long) 10 / カスタマイズ項目(long)10      |
| AVD_PPB_QTY   | NUMBER(9,0)         | Yes      |              | 150       | Number Of Pieces Per Ball / ボール入数                      |
| AVD_SBL_QTY   | NUMBER(9,0)         | No       | 0            | 151       | Ball QTY(Sched.) / ボール予定数                              |
| AVD_PCUM_BL   | NVARCHAR2(3 CHAR)   | Yes      |              | 152       | UOM (Ball) / 単位(Ball数)                                 |
