# GWH_TM_PIK
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | PIK_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | PIK_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | PIK_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | PIK_NUM | NUMBER(1) | N | Picking Key Code / Picking Key NO |
| 22 | PIK_USE_FLG | CHAR(1) | N | Picking Key use Flag / Picking Key使用フラグ |
| 23 | PIK_NAM | NVARCHAR2(100) | Y | Picking Key Name / Picking Key名称 |
| 24 | PIK_TYPE_KND | CHAR(2) | Y | Picking Key Type Kind / Picking Key属性区分 |
| 25 | PIK_SGDG | NUMBER(2) | Y | Picking Key Significant Digit / Picking Key有効桁数 |
| 26 | PIK_IPFM_KND | CHAR(2) | Y | Picking Key Input Format Kind / Picking Key日付形式区分 |
| 27 | PIK_ALPT_KND | CHAR(2) | Y | Picking Key Allocation Pattern Kind / Picking Key引当パターン区分 |
| 28 | PIK_IPLT_KND | CHAR(1) | N | Picking Key Input Limit Kind / Picking Key入荷時必須フラグ |
| 29 | PIK_DSSP_FLG | CHAR(1) | N | Designated Shipping Flag / Picking Key出荷時必須フラグ |
| 30 | PIK_AUTO_FLG | CHAR(1) | N | Automatic Setting Flag / Picking Key入荷日自動セットフラグ |
| 31 | PIK_DUDT_FLG | CHAR(1) | N | Due Date Control Flg / Picking Key期限管理フラグ |
| 32 | PIK_AVSC_KND | CHAR(1) | N | Arrival Scan Kind / Picking Key入荷スキャン区分 |
| 33 | PIK_PISC_KND | CHAR(1) | N | Picking Scan Kind / Picking Key出荷Pickingスキャン区分 |
| 34 | PIK_PASC_KND | CHAR(1) | N | Packing Scan Kind / Picking Key出荷Packingスキャン区分 |
| 35 | PIK_LOSC_KND | CHAR(1) | N | Loading Scan Kind / Picking Key Loadingスキャン区分 |
| 36 | PIK_BARL_KIND | CHAR(1) | Y | Barcode Length Kind / BCD固定長可変長区分 |
| 37 | PIK_STRP | NUMBER(3) | Y | Start Position(Fixed) / 開始位置(固定) |
| 38 | PIK_LENF | NUMBER(3) | Y | Length(Fixed) / 桁数(固定) |
| 39 | PIK_SPRT | NVARCHAR2(6) | Y | separator(Variable) / セパレーター(可変) |
| 40 | PIK_POSV | NUMBER(3) | Y | Column(Variable) / カラム位置(可変) |
| 41 | PIK_LCSC_KND | CHAR(1) | N | Location Entry Scan Kind/Picking Key入荷棚付スキャン区分 |
| 42 | PIK_RLSC_KND | CHAR(1) | N | Relocation Entry Scan Kind/Picking Keyリロケーションスキャン区分 |
| 43 | PIK_BEBY_KND | CHAR(1) | Y | Best By Date Control Kind |
